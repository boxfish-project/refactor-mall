/**
 * Created by malu on 16/7/19.
 */
//提交按钮
$("#btnSubmit").on('click', function () {
    $("#processing").removeClass("hidden");
    var skuAmount = $('#skuAmount').val();//单品数量
    var skuCycle = $('#skuCycle').val();//周期
    var actualPrice = $("#actualPrice").val();//套餐价格
    if(skuAmount == "" || skuAmount == 0 || skuCycle == "" || skuCycle == 0|| actualPrice == ""){
        $("#errorMsg").css("color", "red");
        $("#errorMsg").html("表格数据不能为空!");
        return false;
    }
    var comboVo = '{ "skuId":"' + $skuId + '","skuAmount":"' + skuAmount+ '","actualPrice":"' + actualPrice + '","skuCycle":"' + skuCycle+ '"}';
    $.ajax({
        url: '/admin/create',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        data: comboVo,
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
        url: "/admin/list?access_token=" + $accessToken + "&skuId=" + $skuId,
        page: 1
    }).trigger('reloadGrid');
}

$(function ($) {
    $.ajax({
        url:  "/admin/queryUnitPrice/"+$skuId,
        type: 'get',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        success: function (result) {
            $("#unitPrice").val(result.data);
        },
        error: function (errorMsg) {
            console.log(errorMsg);
        }
    });
});

//套餐价格选择,所填数据为整数
$("#actualPrice").on("keyup", function () {
    var value = $(this).val();
    value = value.replace(/[^\d]/g,"");
    value = value -1 + 1;//"01"->"1"
    $(this).val(value);
});

//单品数量选择,0-100之间的整数
$("#skuAmount").on("keyup", function () {
    var unitPrice = $("#unitPrice").val();
    var value = $(this).val();
    value = value.replace(/[^\d]/g,"");
    value = value -1 + 1;//"01"->"1"
    if(value >= 100){
        value = 100;
    }
    $(this).val(value);
    $("#actualPrice").val(unitPrice*value);
});

//"-/+"操作
$("#btn, #btnPlus").on("click", function () {
    var unitPrice = $("#unitPrice").val();
    var amount = $("#skuAmount").val();
    var id = $(this).attr("id");
    if(id == "btn" && amount >= 2){
        $("#skuAmount").val(amount - 1);
        $("#actualPrice").val(unitPrice*(amount - 1));
    }
    if(id == "btnPlus" && amount <= 9){
        //获取到的amount可能为字符串,"+"处理前进行"-"操作
        $("#skuAmount").val(amount -1 + 2);
        $("#actualPrice").val(unitPrice*(amount - 1 + 2));
    }
});

//单品数量选择,0-100之间的整数
$("#skuCycle").on("keyup", function () {
    var unitPrice = $("#unitPrice").val();
    var value = $(this).val();
    value = value.replace(/[^\d]/g,"");
    value = value -1 + 1;//"01"->"1"
    if(value >= 12){
        value = 12;
    }
    $(this).val(value);
    $("#actualPrice").val(unitPrice*value);
});

//"-/+"操作
$("#btnC, #btnPlusC").on("click", function () {
    var unitPrice = $("#unitPrice").val();
    var skuCycle = $("#skuCycle").val();
    var id = $(this).attr("id");
    if(id == "btnC" && skuCycle >= 2){
        $("#skuAmount").val(skuCycle - 1);
        $("#actualPrice").val(unitPrice*(skuCycle - 1));
    }
    if(id == "btnPlusC" && amount <= 11){
        //获取到的amount可能为字符串,"+"处理前进行"-"操作
        $("#skuCycle").val(skuCycle -1 + 2);
    }
});