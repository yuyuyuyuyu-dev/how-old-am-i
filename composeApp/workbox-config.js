module.exports = {
    globDirectory: "build/dist/wasmJs/productionExecutable/",
    globPatterns: [
        "**/*",
    ],
    swDest: "build/dist/wasmJs/productionExecutable/serviceWorker.js",
    maximumFileSizeToCacheInBytes: 10 * 1024 * 1024,
};
