$(function () {
    //点击不同的dt标签 可以将dt标签最近的div显示 隐藏其他非最近的div
    $("#myMenu dl dt").click(function () {
        //获取最近的div
        var dd = $(this).next("div");//获取当前标签紧挨着的一个标签 这里指明获取最近的div标签
        //获取最近的div的display属性
        var display = dd.css("display");
        //实现切换显示
        if (display=="none"){
            //当前div显示 其他div显示
            //隐藏id是myMenu中所有div 当某个dt的下标和当前div下标相同则展示他的div
            $("#myMenu div").hide().eq($("#myMenu dt").index(this)).show();
        }else{

        }
    });

})