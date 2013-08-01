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
            padding: 25px;
            float: left;
            overflow: hidden;
            width: 95%;
        }

        .content-label {
            margin-bottom: 5px;
            margin-right: 10px;
            padding-top: 4px;
            text-align: right;
            width: 14em;
            font-size: 12px;
            min-height: 20px;
            vertical-align: top;
        }

        .section-header {
            background-color: #FFFFFF;
            border: 1px solid #CCCCCC;
            color: black;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 13px;
            font-style: normal;
            text-align: left;
            padding: 10px 0 10px 10px;
        }

        .section-header-text {
            color: #444455;
        }

        .race-category-area {
            display: inline-block;
            vertical-align: top;
        }

        .race-category-header {
            padding: 5px;
            font-size: 13px;
            font-weight: bold;
        }

        #ethnicity {
            padding: 4px;
        }

        .races-content {
            padding: 4px;
        }

        .button-area {
            float: right;
        }

        #save-btn, #ask-me-later-btn {
            cursor: pointer;
        }

        #errorMessage {
            font-size: 13px;
            font-weight: bold;
            padding: 4px;
            color: red;
        }

    </style>
    <script>
    $(document).ready(function () {
        $("#save-btn").click(function () {
            if ($('#ethnicity input:checked').length > 1) {
                $("#errorMessage").empty()
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

    </script>
</head>

<body>
<div id="content">
    <div id="bodyContainer" class="ui-layout-center inner-center">
        <div id="pageheader" class="level4">
            <div id="pagetitle"><g:message code="survey.title" /></div>
        </div>
        <div id="pagebody" class="level4">
            <div id="contentHolder">
                <div id="contentBelt"></div>
                <div class="pagebodydiv" style="display: block;">
                    <div id="errorMessage"></div>
                    <form controller="survey" action="save" id='surveyForm' method='POST'>
                        <div id="ethnicity-race-wrapper">
                            <div id="ethnicity-wrapper">
                                <div id="ethnicity-header" class="section-header">
                                    <span class="section-header-text"><g:message code="survey.ethnicity.header" /></span>
                                </div>
                                <div id="ethnicity">
                                    <input id="chkEthn_1" name="ethnicity" value="1" type="checkbox" <g:if test="${personEthnicity == '1'}">checked="true"</g:if> />
                                    <label for="chkEthn_1" class="content-label"><g:message code="survey.ethnicity.nothispanic" /></label>
                                    <br/>
                                    <input id="chkEthn_2" name="ethnicity" value="2" type="checkbox" <g:if test="${personEthnicity == '2'}">checked="true"</g:if> />
                                    <label for="chkEthn_2" class="content-label"><g:message code="survey.ethnicity.hispanic" /></label>
                                </div>
                            </div>
                            <div id="race-wrapper">
                                <div id="race-header" class="section-header">
                                    <span class="section-header-text"><g:message code="survey.race.header" /></span>
                                </div>
                                <g:each in="${regulatoryRaces}" var="regulatoryRace">
                                    <div id="race-category_${regulatoryRace.code}" class="race-category-area">
                                        <div id="race-category-desc" class="race-category-header">${regulatoryRace.description}</div>
                                        <div id="races_${regulatoryRace.code}" class="races-content">
                                            <g:each in="${raceMap[regulatoryRace.code]}" var="race">
                                                <input id="chkRace_${race.race}" name="race" value="${race.race}" type="checkbox" <g:if test="${personRaceCodes.contains(race.race)}">checked="true"</g:if>/>
                                                <label for="chkRace_${race.race}" class="content-label">${race.description}</label>
                                                <br/>
                                            </g:each>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                        <div class="button-area">
                            <input type='button' value="Ask me Later" id="ask-me-later-btn" class="secondary-button"/>
                            <input type='button' value="Continue" id="save-btn" class="primary-button"/>
                        </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
