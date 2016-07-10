<script type="text/javascript">
<!--
function moveFolder(){
    var folder = $('#targetselect option:selected').text();
    $('#targetFolder').val(folder);
    $('#folderform').attr('action','Folder.jwma?moveFolder').submit();
}

function newFolder(){
    var ndx = $('#storetype option:selected').index();
    $('#newStore').val(ndx);
    $('#folderform').submit();
}

function updateList(){}
// -->
</script>
