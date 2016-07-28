/**
 * Created by malu on 16/7/19.
 */
//提交按钮
$("#btnSubmit").on('click', function () {
    $("#processing").removeClass("hidden");
    var serviceName = $('#serviceName').val();//服务名称
    // var flagEnable = $('#flagEnable').val();//是否可用
    var description = $('#description').val();//描述
    if(serviceName == ""){
        $("#errorMsg").css("color", "red");
        $("#errorMsg").html("serviceName和flagEnable不能为空!");
        return false;
    }
    var skuKey = '{ "serviceName":"' + serviceName + '","description":"' + description + '"}';
    console.log(skuKey);
    $.ajax({
        url: '/admin/services/create',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        data: skuKey,
        success: function (result) {
            if(result.data){
                reloadTable();
                $("#btnClose").click();
            }
        },
        error: function (errorMsg) {
            console.log(errorMsg);
            $("#errorMsg").css("color", "red");
            //noinspection JSUnresolvedVariable
            $("#errorMsg").html($.parseJSON(errorMsg.responseText).returnMsg);
        }
    });
});

//按查询条件重新加载表格数据
function reloadTable() {
    $("#grid-table").jqGrid('setGridParam', {
        datatype: "json",
        url: "/admin/services/list",
        page: 1
    }).trigger('reloadGrid');
}
