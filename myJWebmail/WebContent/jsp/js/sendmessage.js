
<script type="text/javascript">
<!--
function submitsave(){
    var to = $('#to').text();
    var subject = $('#subject').text();;
    var body = $('#txtbody').text();
    $('#sendform').submit();
}

function richtext(){
    make_wyzz('txtbody');
    $('#saveDraft').attr("disabled", "disabled");
    $('#richText').val('true');
    $('#rich').hide('slow');    
    return false;
}

function checkattachment(){
    var attach = $('#attachment');
    if(attach.val() != ""){    
        $('#attachment1').show('slow');
    }
}

$(function(){window.setInterval("checkattachment()", 10000);});

$(function(){$('#attachment1').hide();});

function updateList(){}
// -->
</script>

<script language="JavaScript" type="text/javascript" src="wyzz.js"></script>
