import os
import re
import sys
import urllib.request
import xml.etree.ElementTree as ElementTree

MAVEN = "https://repo1.maven.org/maven2"
CHANGELOG = "https://raw.githubusercontent.com/JetBrains/compose-multiplatform/master/CHANGELOG.md"
CATALOG = "gradle/libs.versions.toml"
POM_NAMESPACE = {"pom": "http://maven.apache.org/POM/4.0.0"}
STABLE = re.compile(r"^(\d+)\.(\d+)\.(\d+)$")
KNOWN = re.compile(r"^(\d+)\.(\d+)\.(\d+)(?:-(alpha|beta|rc)(\d+))?$")
STAGES = {"alpha": 0, "beta": 1, "rc": 2, None: 3}


def fetch(url):
    with urllib.request.urlopen(url, timeout=60) as response:
        return response.read().decode()


def versions(path):
    metadata = ElementTree.fromstring(fetch(f"{MAVEN}/{path}/maven-metadata.xml"))
    return [element.text for element in metadata.iter("version")]


def order(version):
    match = KNOWN.match(version)
    if not match:
        raise ValueError(f"Unrecognised version {version}")
    major, minor, patch, stage, number = match.groups()
    return int(major), int(minor), int(patch), STAGES[stage], int(number or 0)


def line(version):
    return version.split(".")[0:2]


def latest_stable_compose():
    stable = [v for v in versions("org/jetbrains/compose/compose-gradle-plugin") if STABLE.match(v)]
    return max(stable, key=order)


def compose_requirement(material3):
    path = f"org/jetbrains/compose/material3/material3/{material3}/material3-{material3}.pom"
    pom = ElementTree.fromstring(fetch(f"{MAVEN}/{path}"))
    required = []
    for dependency in pom.iter("{http://maven.apache.org/POM/4.0.0}dependency"):
        group = dependency.findtext("pom:groupId", namespaces=POM_NAMESPACE)
        version = dependency.findtext("pom:version", namespaces=POM_NAMESPACE)
        if not group or not version:
            continue
        if group.startswith("org.jetbrains.compose.") and not group.startswith("org.jetbrains.compose.material3"):
            required.append(version)
    return max(required, key=order) if required else None


def paired_material3(compose):
    candidates = [
        v
        for v in versions("org/jetbrains/compose/material3/material3")
        if KNOWN.match(v) and line(v) == line(compose)
    ]
    for candidate in sorted(candidates, key=order, reverse=True):
        requirement = compose_requirement(candidate)
        if requirement and order(requirement) <= order(compose):
            return candidate
    raise SystemExit(f"No material3 in the {'.'.join(line(compose))} line requires Compose {compose} or older")


def paired_adaptive(compose):
    sections = re.split(r"^# ", fetch(CHANGELOG), flags=re.MULTILINE)
    section = next((s for s in sections if s.startswith(f"{compose} ")), None)
    if section is None:
        return None, "pending"
    match = re.search(r"org\.jetbrains\.compose\.material3\.adaptive:adaptive\*:([^`\s]+)", section)
    if not match or not KNOWN.match(match.group(1)):
        return None, "unparseable"
    return match.group(1), "found"


def set_version(catalog, key, version):
    pattern = re.compile(rf'^{re.escape(key)} = "[^"]*"$', re.MULTILINE)
    if len(pattern.findall(catalog)) != 1:
        raise SystemExit(f"Expected exactly one {key} version in {CATALOG}")
    return pattern.sub(f'{key} = "{version}"', catalog)


def main():
    compose = latest_stable_compose()
    material3 = paired_material3(compose)
    adaptive, adaptive_status = paired_adaptive(compose)

    with open(CATALOG) as file:
        catalog = file.read()
    catalog = set_version(catalog, "composeMultiplatform", compose)
    catalog = set_version(catalog, "material3", material3)
    if adaptive:
        catalog = set_version(catalog, "compose-multiplatform-adaptive", adaptive)
    with open(CATALOG, "w") as file:
        file.write(catalog)

    outputs = {
        "compose": compose,
        "material3": material3,
        "adaptive": adaptive or "",
        "adaptive-status": adaptive_status,
    }
    lines = "".join(f"{key}={value}\n" for key, value in outputs.items())
    output = os.environ.get("GITHUB_OUTPUT")
    if output:
        with open(output, "a") as file:
            file.write(lines)
    sys.stdout.write(lines)


if __name__ == "__main__":
    main()
