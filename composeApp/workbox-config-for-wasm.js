module.exports = {
    globDirectory: "build/dist/wasmJs/productionExecutable/",
    globPatterns: ["**/*.{html,css,js,json,png,jpg,jpeg,svg,gif,webp,woff,woff2,wasm}"],
    maximumFileSizeToCacheInBytes: 10 * 1024 * 1024,
    skipWaiting: true,
    clientsClaim: true,
    swDest: "build/dist/wasmJs/productionExecutable/serviceWorker.js",
};
