self.addEventListener('install', (e) => {
    e.waitUntil(
        caches.open('how-old-am-i').then((cache) => cache.addAll([
            '/how-old-am-i/',
            '/how-old-am-i/index.html',
            '/how-old-am-i/styles/style.css',
            '/how-old-am-i/styles/fonts/Nyashi.woff2',
            '/how-old-am-i/styles/fonts/Jiyucho.woff2',
            '/how-old-am-i/scripts/main.js',
            '/how-old-am-i/scripts/wasm/wasm_exec.js',
            '/how-old-am-i/scripts/wasm/run_wasm.js',
            '/how-old-am-i/go/go.wasm',
        ])),
    );
});


self.addEventListener('fetch', function(event) {
    event.respondWith(
        caches.match(event.request).then(function(response) {
            return response || fetch(event.request);
        })
    );
});
