const year = document.getElementById("year");
const month = document.getElementById("month");
const day = document.getElementById("day");

const outputField = document.getElementById("outputField");


function main() {
    // 入力欄になにかしら入力されたときの処理を定義する
    year.addEventListener("input", () => {
        showResult();

        if (year.value.length >= 4 && isFinite(year.value)) {
            month.focus();
        }
    });

    month.addEventListener("input", () => {
        showResult();

        if (month.value.length >= 2 && isFinite(month.value) && 1 <= month.value && month.value <= 12) {
            day.focus();
        }
    });

    day.addEventListener("input", () => {
        showResult();
    });

    // WebAssemblyの準備が終わるまで「読込中...」と表示しておく
    const nowLoading = document.getElementById("nowLoading");
    nowLoading.style.display = "none";
    const inputField = document.getElementById("inputField");
    inputField.removeAttribute("hidden");
    const pleaseEnterYourDateOfBirth = document.getElementById("PleaseEnterYourDateOfBirth");
    pleaseEnterYourDateOfBirth.removeAttribute("hidden");
}


function showResult() {
    outputField.textContent = howOldAmI(year.value, month.value, day.value);
}


mainWasLoaded = true;
if (wasmWasLoaded && mainWasloaded) {
    main();
}
