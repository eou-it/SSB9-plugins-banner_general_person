<%@ page contentType="text/html;charset=UTF-8" %>
<%--
/*******************************************************************************
Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
--%>
<html>
<head>
    <title>Race and Ethnicity Survey</title>
    <meta name="layout" content="bannerSelfServicePage"/>

    <style type="text/css">

        #bodyContainer {
            margin: 30px;
        }

        #pageheader {
            width: 100%;
            margin: 25px 0;
            padding: 0px;
        }

        #pagetitle {
            float: left;
            color: #646464;
            font-size: 27px;
            font-weight: normal;
            letter-spacing: 0.08em;
            padding: 0;
            margin: 5px 0 10px 0;
            width: 85%;
        }

        #pagebody {
            padding: 0 0 25px 0px;
            top: -5px;
            background: #fff url(images/content-bg-tc-dark.png) repeat-x left top;
            border: 1px #e5ebee solid;
            border-top-width: 0px;
            float: left;
            min-width: 911px;
            width: 100%;
            margin: 29px 0 0;
        }

        #pagebody.level4 {
            min-width: 100%;
            width: auto;
            margin: 0;
        }

        #contentHolder {
            background: transparent;
            position: relative;
            top: 0px;
            padding: 25px; /* safari fix */
            float: left;
            overflow: hidden;
            width: 95%;
        }

        #contentBelt {

        }

        #contentBelt div {

        }

        DIV.pagebodydiv {
            text-align: left;
        }

        DIV.infotextdiv {
            text-align: left;
        }

        .infotextdiv {
            background-color: #FFFFFF;
            border: 1px solid #CCCCCC;
        }

        .infotexttable {
            font-size: 0.98em;
            line-height: 13pt;
        }

        .race-row {
            /*float: left;*/
            margin-bottom: 5px;
            margin-right: 10px;
            padding-top: 4px;
            text-align: right;
            width: 14em;
            font-size: 12px;
            min-height: 20px;
        }

    </style>
    %{--<r:require modules="term,registration"/>--}%
    <script>
    $(document).ready(function () {
        $("#save-btn").click(function () {
            if ($('#ethnicity input:checked').length > 1) {
                $("#errorMessage").append("You may only select one checkbox for Ethnicity")
            }
            else {
                $("#errorMessage").empty()
                var form = document.getElementById('surveyForm')
                form.submit()
            }
        });

        $("#ask-me-later-btn").click(function () {
            window.location = "${createLink(controller: "survey", action: "completed")}";
        });

    })

    /*function submitSurvey() {
        if ($('#ethnicity input:checked').length > 1) {
            alert("You may only select one checkbox for Ethnicity")
        }
        else {
            alert('else')
            var form = document.getElementById('surveyForm')
            form.submit()
        }
    }


    function askMeLater() {
        alert('ask later')
    }*/

    /*function moveToController() {
        window.location = "${createLink(controller: "survey", action: "completed")}";
    }*/

    </script>
</head>

<body>
<div id="content">
    <div id="bodyContainer" class="ui-layout-center inner-center">
        <div id="pageheader" class="level4">
            <div id="pagetitle">Update Ethnicity and Race</div>
        </div>
        <div id="pagebody" class="level4">
            <div id="contentHolder">
                <div id="contentBelt"></div>
                <div class="pagebodydiv" style="display: block;">
                    <div id="errorMessage"></div>
                    <form controller="survey" action="save" id='surveyForm' method='POST'>
                    %{--<g:form action='${postUrl}' method='POST' id='surveyForm'>--}%
                        <div id="ethnicity-race-wrapper">
                            <div id="ethnicity-wrapper">
                                <div id="ethnicity-header">What is your ethnicity?</div>
                                %{--<g:each in="${ethnicityList}" var="ethnicity">--}%
                                    <div id="ethnicity">
                                        %{--<input id="rdEthn_${ethnicity}" name="ethnicity" type="checkbox" value="${ethnicity}"/>
                                        <label class="form-row" for="ethnicity-desc_${ethnicity}">${ethnicity}</label>--}%
                                        <input id="chkEthn_1" name="ethnicity" value="1" type="checkbox" <g:if test="${personEthnicity == '1'}">checked="true"</g:if> />
                                        <label for="chkEthn_1" class="race-row"><g:message code="survey.ethnicity.nothispanic" /></label>
                                        <br/>
                                        <input id="chkEthn_2" name="ethnicity" value="2" type="checkbox" <g:if test="${personEthnicity == '2'}">checked="true"</g:if> />
                                        <label for="chkEthn_2" class="race-row"><g:message code="survey.ethnicity.hispanic" /></label>
                                    </div>
                                %{--</g:each>--}%
                            </div>
                            <div id="race-wrapper">
                                <div id="race-header">Select one or more races to indicate what you consider yourself to be.</div>
                                <g:each in="${regulatoryRaces}" var="regulatoryRace">
                                    <div id="race-category_${regulatoryRace.code}" style="display: inline-block">
                                        <div id="race-category-desc">${regulatoryRace.description}</div>
                                        <div id="races_${regulatoryRace.code}">
                                            <g:each in="${raceMap[regulatoryRace.code]}" var="race">
                                                <input id="chkRace_${race.race}" name="race" value="${race.race}" type="checkbox" <g:if test="${personRaceCodes.contains(race.race)}">checked="true"</g:if>/>
                                                <label for="chkRace_${race.race}" class="race-row">${race.description}</label>
                                                <br/>
                                            </g:each>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                            <input type='button' value="Continue" id="save-btn" class="primary-button" />
                            <input type='button' value="Ask me Later" id="ask-me-later-btn" class="secondary-button" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
%{--Take Survey
<input type="button" value="yes" onclick="moveToController()"/>--}%
</body>
</html>
