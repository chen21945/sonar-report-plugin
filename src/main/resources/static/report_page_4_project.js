window.registerExtension('reports/report_page_4_project', function (options) {

    // let's create a flag telling if the pages is still displayed
    var isDisplayed = true;

    if (isDisplayed) {
        //add page content
        var template = document.createElement("div");
        template.setAttribute("id", "template");
        options.el.appendChild(template);

        $('#template').load("/static/reports/pages/report.html", function () {
            //button function
            document.querySelector('#generation').onclick = function () {
                // lock the form
                setEnabled(false);
                //get selected types
                var types = getIssueTypes();
                var severities = getSeverities();

                window.SonarRequest.request('/api/reports/pdf')
                    .setMethod('GET')
                    .setData({key: options.component.key, types: types, severities: severities})
                    .submit()
                    .then(function (value) {
                        return value.blob();
                    })
                    .then(function (blob) {
                        var url = window.URL.createObjectURL(blob);
                        var a = document.createElement('a');
                        a.download = options.component.key + '.pdf';
                        a.href = url;
                        options.el.appendChild(a);
                        a.click();
                        a.remove();
                        console.log("Generate PDF success \n");
                        setEnabled(true);
                    }).catch(function (error) {
                    // log error
                    console.log("[ERROR] Export failed." + error);
                    setEnabled(true);
                });
            };
        });
    }


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

    var getIssueTypes = function () {
        //get all selected checkbox value and splice then with ","
        var types = "";
        $("#itype-div input[type='checkbox']:checked").each(function (index, item) {
            types += $(this).val() + ",";
        });
        return types.length > 0 ? types.substr(0, types.length - 1) : types;
    };

    var getSeverities = function () {
        //get all selected checkbox value and splice then with ","
        var types = "";
        $("#severity-div input[type='checkbox']:checked").each(function (index, item) {
            types += $(this).val() + ",";
        });
        return types.length > 0 ? types.substr(0, types.length - 1) : types;
    };

    // return a function, which is called when the pages is being closed
    return function () {
        // we unset the `isDisplayed` flag to ignore to Web API calls finished after the pages is closed
        isDisplayed = false;
        options.el.textContent = '';
    };
});
