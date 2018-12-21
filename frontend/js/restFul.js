
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

function loadAddFormTemplate(){
    document.getElementById("form-add").style.display='block';
    document.getElementById("table-area").style.display='none';

}

function cancelarAdicionar(){
    document.getElementById("form-add").style.display='none';
    document.getElementById("table-area").style.display='block';
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

