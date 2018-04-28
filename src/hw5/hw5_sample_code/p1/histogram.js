(() => {
  const NUM_WORKERS = 4;
  const MAX_VALUE = 1000;
  const NUM_SAMPLES = 100000;
  const SAMPLES_PER_WORKER = NUM_SAMPLES / NUM_WORKERS;
  const RANGE_PER_THREAD = MAX_VALUE / NUM_WORKERS;

  let idGen = 0;
  let results = [];
  let promises = [];
  for(let i = 0; i < NUM_WORKERS; i++){
    let buffer = new ArrayBuffer(RANGE_PER_THREAD * 4); // bytes per int
    promises[i] = invokeAsync(calcHist, [buffer, SAMPLES_PER_WORKER, RANGE_PER_THREAD], new Worker('worker.js'));
  }

  let index = 0;
  for(let i = 0; i < promises.length; i++){
    promises[i].promise.then((result) => {
      let temp = new Int32Array(results.length + result.length);
      temp.set(results);
      temp.set(result, results.length);
      results = temp;
      console.log(results);
    }, (error) => {
      console.log(error);
    })
  }

  function invokeAsync(f, args, worker){
    let p = new Promise((resolve, reject) => {
      let id = 'id' + idGen++;
      worker.postMessage([id, f.toString(), args]);
      worker.onmessage = (e) => resolve(new Int32Array(e.data[2]));
    });
    return {
      promise: p,
      buffer: args[0],
    }
  }

  function calcHist(buffer, samples, maxVal){
    let threadSpecificResults = new Int32Array(buffer);
    for (let i = 0; i < samples; ++i) {
      threadSpecificResults[parseInt(Math.floor(Math.random() * Math.floor(maxVal)), 10)]++;
    }
    return buffer;
  }
})();
