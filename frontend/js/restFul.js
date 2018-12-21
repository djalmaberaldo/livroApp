
const url='http://localhost:8081/livro/api/livros';


function loadAllData(){

    fetch(url,{
        method: 'GET'
    })
    .then(function(response){
        return response.json();
    })
    .then(function(response) { 
        fillTable(response);
    })
    .catch(function(err) { console.error(err); });
}

function loadAddFormTemplate(){
    document.getElementById("form-add").style.display='block';
    document.getElementById("table-area").style.display='none';

}

function fillTable(livros){
    var rows = "";
    for (var key in livros){
        var contentRow = "<tr><td>"+livros[key].autor+"</td>"
        + "<td>"+livros[key].titulo+"</td>"
        +"<td>"+livros[key].isbn+"</td>"
        +"<td>"+livros[key].preco+"</td>"
        +"<td>"+livros[key].edicao+"</td>"
        +"<td>"+livros[key].editora+"</td>"
        +"<td></td></tr>"
        rows = rows + contentRow;
    }

    document.getElementById("content-row").innerHTML = rows;
}

function cancelarAdicionar(){
    document.getElementById("form-add").style.display='none';
    document.getElementById("table-area").style.display='block';
}

function insertData(){
    var formData = {"titulo": document.getElementById("titulo").value,
    "autor": document.getElementById("autor").value,
    "isbn": document.getElementById("isbn").value,
    "preco": document.getElementById("preco").value,
    "edicao": document.getElementById("edicao").value,
    "editora": document.getElementById("editora").value,
    "id": null};

    var headers = new Headers();
    headers.append("Content-type","application/json")

    fetch(url,{
        method: 'POST',
        body: JSON.stringify(formData),
        headers: headers
    })
    .then(function(response) { 
        response.text()
        .then(function(result){ 
            document.getElementById("table-area").style.display='block';
            document.getElementById("form-add").style.display='none';
            loadAllData();
        }) 
    })
    .catch(function(err) { console.error(err); });
}

