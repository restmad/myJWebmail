<script type="text/javascript">
<!--

$(document).ready(function(){
    var index = $('#sortOrder');
    var sort = parseInt(index.val());
    $('#listMessages > option').eq(sort).attr('selected','selected');
});

function sort_order(){
    var index = $("#listMessages option").index($("#listMessages option:selected"));
    $('#sortOrder').val(index);
    $('#form1').attr('action','Folder.jwma?listMessages').submit();
}

function selectall(){
     for(var i=0; i<form3.selected.length; i++){
         form3.selected[i].checked=!form3.selected[i].checked;
     }
}

function searchwhat(){
    if($('#searchkey').value != ""){
        $('#form2').submit();
    }
}

function submitMove(form){
    with(document.form3){
        var i = document.form3.destination.selectedIndex;
        document.form3.moveTo.value = document.form3.destination[i].text;
        document.form3.action ="Folder.jwma?moveMessage";
        document.form3.submit()
    }
}

function updateList(){
    if($('#folder').val() == "INBOX"){
        var link = 'Folder.jwma?listMessages=&folder=INBOX&parent= #list';
        $('#list').load(link);
    }
}

// -->
</script>