const year = document.getElementById("year");
const month = document.getElementById("month");
const day = document.getElementById("day");

const outputField = document.getElementById("outputField");


function main() {
    // WebAssemblyの準備が終わるまで「読込中...」と表示しておく
    const nowLoading = document.getElementById("nowLoading");
    nowLoading.style.display = "none";
    const inputField = document.getElementById("inputField");
    inputField.removeAttribute("hidden");

    // 入力欄になにかしら入力されたときの処理を定義する
    year.addEventListener("input", () => {
        showResult();

        if (year.value.length >= 4) {
            month.focus();
        }
    });

    month.addEventListener("input", () => {
        showResult();

        if (month.value.length >= 2) {
            day.focus();
        }
    });

    day.addEventListener("input", () => {
        showResult();
    });
}


function showResult() {
    outputField.textContent = howOldAmI("hoge", "fuga", "piyo", "2022");
}


mainWasLoaded = true;
if (wasmWasLoaded && mainWasloaded) {
    main();
}
