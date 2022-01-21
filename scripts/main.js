function main() {
    // WebAssemblyの準備が終わるまで「読込中...」と表示しておく
    const nowLoading = document.getElementById("nowLoading");
    nowLoading.style.display = "none";
    const inputField = document.getElementById("inputField");
    inputField.removeAttribute("hidden");

    // 入力欄になにかしら入力されたときの処理を定義する
    const year = document.getElementById("year");
    const month = document.getElementById("month");
    const day = document.getElementById("day");

    year.addEventListener("input", () => {
        if (year.value.length >= 4) {
            month.focus();
        }
    });

    month.addEventListener("input", () => {
        if (month.value.length >= 2) {
            day.focus();
        }
    });
}


mainWasLoaded = true;
if (wasmWasLoaded && mainWasloaded) {
    main();
}
