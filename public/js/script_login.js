/**
 * Created by jorgerubiano on 6/03/16.
 */
$(function()
{
    $( "#form" ).submit(function( event )
    {
        var dataUser = {
                            correo : $("#inputEmail").val(),
                            clave : $("#inputPassword").val()
        };
        $.ajax(
        {
            url 		: "/login",
            type 		: "POST",
            data 		: JSON.stringify(dataUser),
            dataType 	: "json",
            contentType: "application/json; charset=utf-8"
        }).done(function(data)
        {
            console.log(data);
            window.location.href = "/catalog"
        }).error(function(request, status, error)
        {
            sweetAlert("Error", "No ha sido posible realizar la autenticación!", "error");
        });
        event.preventDefault();
    });
});