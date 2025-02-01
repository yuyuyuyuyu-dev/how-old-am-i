module.exports = {
    globDirectory: "build/dist/wasmJs/productionExecutable/",
    globPatterns: [
        "**/*",
    ],
    swDest: "build/dist/wasmJs/productionExecutable/serviceWorker.js",
    runtimeCaching: [{
        handler: "CacheFirst",
        urlPattern: /.+/,
    }],
};