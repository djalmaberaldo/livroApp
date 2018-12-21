
function loadAllData(){
    const url='http://localhost:8081/livro/api/livros';

    fetch(url,{
        method: 'GET'
    })
    .then(function(response) { 
        response.text()
        .then(function(result){ 
           console.log(result); 
         }) 
    })
    .catch(function(err) { console.error(err); });
}

function insertData(){
    const url='http://localhost:8080/api/livros';
    const myHeaders = new Headers();

    myHeaders.append('Origin', 'http://localhost:8080');

    fetch(url,{
        method: 'GET',
        headers: myHeaders
    })
    .then(function(response) { 
        response.text()
        .then(function(result){ 
           console.log(result); 
         }) 
    })
    .catch(function(err) { console.error(err); });
}

