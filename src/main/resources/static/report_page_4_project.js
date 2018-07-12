window.registerExtension('reports/report_page_4_project', function (options) {

    // let's create a flag telling if the pages is still displayed
    var isDisplayed = true;
    console.log(options);

    // then do a Web API call to the /api/issues/search to get the number of issues
    // we pass `resolved: false` to request only unresolved issues
    // and `componentKeys: options.component.key` to request issues of the given project
    window.SonarRequest.getJSON('/api/issues/search', {
        resolved: false,
        componentKeys: options.component.key
    }).then(function (response) {

        if (isDisplayed) {

            console.log(options.component.key);

            var template = document.createElement("div");
            template.setAttribute("id", "template");
            options.el.appendChild(template);

            $('#template').load("/static/reports/pages/report.html", function () {
                document.querySelector('#generation').onclick = function () {
                    // lock the form
                    setEnabled(false);
                    window.SonarRequest.request('/api/reports/pdf')
                        .setMethod('POST')
                        .setData({key: options.component.key})
                        .submit()
                        .then(function (value) {
                            return value.blob();
                        })
                        .then(function (blob) {
                            var reader = new FileReader();
                            // 转换为base64，可以直接放入href
                            reader.readAsDataURL(blob);
                            reader.onload = function (e) {
                                // 转换完成，创建一个a标签用于下载
                                var a = document.createElement('a');
                                a.download = 'Report.pdf';
                                a.href = e.target.result;
                                options.el.appendChild(a);
                                a.click();
                                a.remove();
                            };
                            // on success log generation
                            Console.log("generate PDF success \n");
                            setEnabled(true);
                        }).catch(function (error) {
                        // log error
                        Console.log("[ERROR] Export failed.");
                        setEnabled(true);
                    });
                };
                //fill out project's drop down list
                initProfilesDropDownList();
            });
        }
    });

    var setEnabled = function (isEnabled) {
        // retrieve the form
        var form = document.getElementById("generation-form");
        // get all the components of the form
        var elements = form.elements;
        // change all components readOnly field to (un)lock them
        for (var i = 0, len = elements.length; i < len; i++) {
            elements[i].readOnly = !isEnabled;
            elements[i].disabled = !isEnabled;
        }

        if (isEnabled) {
            // hide loading when button are enabled
            $('#loading').hide();
        } else {
            // show loading otherwise
            $('#loading').show();
        }
    };

    var initProfilesDropDownList = function () {
        window.SonarRequest.getJSON(
            '/api/qualityprofiles/search'
        ).then(function (response) {
            // on success
            // we put each quality gate in the list
            $.each(response.profiles, function (i, item) {
                // we create a new option for each quality gate
                // in the json response
                var option = $('<option>', {
                    value: item.key,
                    text: item.name + ' [' + item.key + ']'
                });
                // we add it to the drop down list
                $('#key').append(option);
            });
        }).catch(function (response) {
            // log error
            console.log("error:" + response)
        });
    };

    // return a function, which is called when the pages is being closed
    return function () {
        // we unset the `isDisplayed` flag to ignore to Web API calls finished after the pages is closed
        isDisplayed = false;
        options.el.textContent = '';
    };
});
