// Copyright 2018 The Go Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

fetch('scripts/wasm/wasm_exec.js').then(r => r.text()).then(t => eval(t)).then(() => {
    if (!WebAssembly.instantiateStreaming) { // polyfill
        WebAssembly.instantiateStreaming = async (resp, importObject) => {
            const source = await (await resp).arrayBuffer();
            return await WebAssembly.instantiate(source, importObject);
        };
    }

    const go = new Go();
    let mod, inst;
    WebAssembly.instantiateStreaming(fetch("go/go.wasm"), go.importObject).then((result) => {
        mod = result.module;
        inst = result.instance;

        run();

        wasmWasLoaded = true;
        if (wasmWasLoaded && mainWasLoaded) {
            main();
        }
    }).catch((err) => {
        console.error(err);
    });

    async function run() {
        await go.run(inst);
        inst = await WebAssembly.instantiate(mod, go.importObject); // reset instance
    }
});
