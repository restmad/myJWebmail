<script type="text/javascript">
<!--
function updateInbox(){
    var link = 'Base.jwma?updateInbox';
    $.get(link, function(data){
        var result = eval(data).split("|");
        $(unread).html(result[0]);
        $(unread).show();
        $(message).html(result[1]);
        $(message).show();
    });
    updateList();
    return false;
}

$(function(){window.setInterval("updateInbox()", 300000);});
// -->
</script>

