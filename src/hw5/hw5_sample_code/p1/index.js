(() => {
  let workerDaemon = new Worker('worker.js');
  let idGen = 0;
  let promises = [];

  invokeAsync(personalizeHello, ['Ethan']).then((result) => {
    console.log(result);
  }, (error) => {
    console.log(error);
  });

  invokeAsync(add, [5, 3]).then((result) => {
    console.log("5 + 3 = " + result);
  }, (error) => {
    console.log(error);
  });

  function personalizeHello(name){
    return "Hello " + name + "!";
  }

  function add(one, two){
    return one + two;
  }

  function invokeAsync(f, args){
    return new Promise((resolve, reject) => {
      let id = 'id' + idGen++;
      workerDaemon.postMessage([id, f.toString(), args]);
      promises[id] = [resolve, reject];
    });
  }

  workerDaemon.onmessage = function(e){
    let status = e.data[0];
    let id = e.data[1];
    let data = e.data[2];

    if(status === 'suc') promises[id][0](data); //resolve
    else promises[id][1](data); //reject
    promises.splice(promises.indexOf(id), 1);
  }
})();
