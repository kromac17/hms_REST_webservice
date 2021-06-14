function checkbox(anfrage_id,manager_id) {
    var col = document.getElementById('ck_'+anfrage_id).firstElementChild.style.backgroundColor;
    if(col != 'green') {
        document.getElementById('ck_' + anfrage_id).firstElementChild.style.backgroundColor = 'green';

        let url = 'http://localhost:8080/hms_REST_webservice_war_exploded/api/resource/'+anfrage_id+'/manager/'+manager_id; //TODO: change to '' when uploaded
        fetch(url, {
            method: 'PUT', // or 'PUT'
            headers: {
                'Content-Type': 'application/json',
            },
        }).then(response =>{
            if(response.status !== 200){
                console.log('Error: '+response.status);
                return;
            }
            return response;
        });

        url = 'http://localhost:8080/hms_REST_webservice_war_exploded/api/resource/'+anfrage_id+'/manager';
        fetch(url)
            .then(response =>{
                if(response.status !== 200){
                    console.log('Error: '+response.status);
                    return;
                }
                return response.json();
            })
            .then(data =>{
                console.log( document.getElementById('ck_' + anfrage_id).parentElement.getElementsByClassName("manager").item(0).innerHTML);
                document.getElementById('ck_' + anfrage_id).parentElement.getElementsByClassName("manager").item(0).innerHTML = data.vorname+' '+data.nachname;
            });
    }
}