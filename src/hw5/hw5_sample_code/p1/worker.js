onmessage = function(e){
  let id = e.data[0];
  let fString = e.data[1];
  let args = e.data[2];

  let i = fString.indexOf("(") + 1;
  let j = fString.indexOf(")", i);
  let params = fString.substring(i, j).split(",");

  i = fString.indexOf("{") + 1 ;
  j = fString.lastIndexOf("}");
  let body = fString.substring(i, j);

  let func = new Function(params, body);

  try{
    postMessage(['suc', id, func.apply(null, args)])
  } catch (error) {
    postMessage(['err', id, error]);
  }
}
