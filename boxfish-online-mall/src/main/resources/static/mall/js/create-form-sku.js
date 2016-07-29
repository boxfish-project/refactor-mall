/**
 * Created by malu on 16/7/23.
 */
/**
 * Created by malu on 16/7/19.
 */
//提交按钮
$("#btnSubmit").on('click', function () {
    $("#processing").removeClass("hidden");
    var serviceId = $('#serviceName').val();//服务类型
    var skuName = $('#skuName').val();//skuName
    var originalPrice = $('#originalPrice').val();//单品价格
    var description = $('#description').val();//单品数量
    if(serviceId == "" || skuName == "" || originalPrice == 0 || originalPrice == ""){
        $("#errorMsg").css("color", "red");
        $("#errorMsg").html("表格数据不能为空!");
        return false;
    }
    var skuCombo = '{ "serviceId":"' + serviceId + '","skuName":"' + skuName+ '","originalPrice":"' + originalPrice + '","description":"' + description+ '"}';
    $.ajax({
        url: '/admin/skus/create',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        data: skuCombo,
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
    $("#grid-table-sku").jqGrid('setGridParam', {
        datatype: "json",
        url: "/admin/skus/list?access_token=" + $accessToken,
        page: 1
    }).trigger('reloadGrid');
}

//加载数据函数
var setSelectData = function (divId, url) {
    if (divId == "" || url == "") {
        alert("无相应服务!");
        return false;
    }
    $.ajax({
        url:  url,
        type: 'get',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        success: function (result) {
            var list = result.data;
            $("#"+divId).html("<option value=''>请选择数据</option>");
            var htmlStr = $("#"+divId).html();

            if(list != ""){
                $.each(list, function (index, item) {
                    console.log(item);
                    htmlStr += "<option value='"+item.id+"'>"+item.text+"</option>";
                });
            }
            $("#"+divId).html(htmlStr);
        },
        error: function (errorMsg) {
            console.log(errorMsg);
        }
    });
}

//加载服务类型下拉框数据
setSelectData("serviceName", "/admin/getServiceNameData");

//价格选择,所填数据为整数
$("#originalPrice").on("keyup", function () {
    var value = $(this).val();
    value = value.replace(/[^\d]/g,"");
    value = value -1 + 1;//"01"->"1"
    $(this).val(value);
});