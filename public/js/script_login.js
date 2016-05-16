/**
 * Created by jorgerubiano on 6/03/16.
 */
$(function()
{
    document.cookie.split('; ').forEach(function(cookieString)
    {
        //console.log(cookieString);
        var cookie = cookieString.split("=");
        console.log(cookie);
        console.log(cookie.length, cookie[0]);
        if ((cookie.length === 2) && (cookie[0] === "authToken"))
        {
            window.authToken = cookie[1];
            console.log(window.authToken);
        }
    });

    var asociaRedesSociales = function(providers) {
        $.getScript("http://cdn.gigya.com/js/gigya.js?apiKey=3_U0malXEBj1336zt95liesG8HWVuHJnLotZJm1SUmTMg5RoThWguHvVVVuxouEhma", function (data, textStatus, jqxhr)
        {
            //console.log(jqxhr.status); // 200
            //console.log("Load was performed.");
            var login_params =
            {
                version: 2,
                showTermsLink: 'false',
                height: 100,
                width: 330,
                containerID: 'componentDiv',
                buttonsStyle: 'fullLogoColored',
                autoDetectUserProviders: '',
                facepilePosition: 'none',
                enabledProviders: providers
            };

            login_params['onClose'] = function (evt) {
                evt['msg'] = 'After onLoad';
                var msg = 'Event name is : ' + evt.eventName + '\n';
                msg += 'evt[\'msg\'] is : ' + evt['msg'] + '\n';
                msg += 'context.msg is : ' + evt['context']['msg'];
                alert(msg);
            };

            //tmoLogin = "";
            gigya.socialize.showLoginUI(login_params);
            function DisplayEventMessage(eventObj) {
                //tmoLogin = eventObj;
                console.log(eventObj.eventName + " event happened");
                if (eventObj.eventName.toLowerCase() === "login") {
                    console.log("Se auténtica");
                    var token = {socialToken: eventObj.user.loginProviderUID};
                    $.ajax(
                        {
                            url: "/sociallogin",
                            type: "POST",
                            data: JSON.stringify(token),
                            dataType: "json",
                            contentType: "application/json; charset=utf-8"
                        }).done(function (data) {
                        console.log("---------");
                        console.log(data);
                        if (Number(data.errorCode) === 1) {
                            //El usuario no existe, se debe direccionar a la opción de crear usuario..
                            localStorage.setItem("socialuser", JSON.stringify(eventObj.user));
                            window.location.href = "/api/usuario/cuentaExterno";
                        }
                        else {
                            localStorage.setItem("user", JSON.stringify(data));
                            window.location.href = "/catalog";
                            console.log(data.authToken);
                        }
                    }).error(function (request, status, error) {
                        sweetAlert("Error", "No ha sido posible realizar la autenticación!", "error");
                    });
                    //Llamar a un servicio que corrobore si el usuario con ese token existe...

                }
            }

            gigya.socialize.addEventHandlers({
                    onLogin: DisplayEventMessage,
                    onConnectionAdded: DisplayEventMessage,
                    onConnectionRemoved: DisplayEventMessage
                }
            );
        });
    };


    //Primero saber si tendrá la opción de redes sociales...
    $.getJSON("api/services/", function(data)
    {
        var redesSociales =  {FaceBook : "facebook", Twitter : "twitter", Google : "googleplus"},
            providers     =  "";
        //console.log("Variabilidad");
        for(var i = 0; i < data[0].childs.length; i++)
        {
            if(data[0].childs[i].present)
            {
                if(providers !== "")
                {
                    providers += ",";
                }
                providers += redesSociales[data[0].childs[i].name];
            }
        }
        //Para saber si tendrá reportes...
        localStorage.setItem("reportes", JSON.stringify(data[3]));
        localStorage.setItem("mensajeria", JSON.stringify(data[5]));
        if(providers !== "")
        {
            console.log(providers);
            asociaRedesSociales(providers);
        }
    });



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
            localStorage.setItem("user", JSON.stringify(data));
            console.log(data);
            window.location.href = "/catalog"
            console.log(data.authToken);
        }).error(function(request, status, error)
        {
            sweetAlert("Error", "No ha sido posible realizar la autenticación!", "error");
        });
        event.preventDefault();
    });
});