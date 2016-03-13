$(function()
{
    //Capturar los datos del producto...
    var servicio = JSON.parse($("#dataType").html());
    document.cookie.split('; ').forEach(function(cookieString)
    {
        //console.log(cookieString);
        var cookie = cookieString.split("=");
        if ((cookie.length === 2) && (cookie[0] === "authToken"))
        {
            window.authToken = cookie[1];
            console.log(window.authToken);
        }
    });
    console.log(servicio);


    $("#logout").click(function(event)
    {
        console.log("LLega", window.authToken);
        $.ajax(
        {
            url 		: "/logout",
            type 		: "POST",
            dataType 	: "json",
            headers     : {"X-AUTH-TOKEN": window.authToken}
        }).done(function(data)
        {
            window.location = "/login";
        }).error(function(request, status, error)
        {
            window.location = "/login";
        });
    });
});
