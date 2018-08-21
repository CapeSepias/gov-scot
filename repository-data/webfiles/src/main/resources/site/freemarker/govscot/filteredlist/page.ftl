<#include "../../include/imports.ftl">

<div class="layout--filtered-list">

<#-- @ftlvariable name="index" type="scot.gov.www.beans.SimpleContent" -->
<#if index??>
    <div class="grid" id="page-content"><!--
        --><div class="grid__item medium--eight-twelfths">
            <h1 class="article-header">${index.title?html}</h1>

            <#if index.content.content?has_content>
                <@hst.html hippohtml=index.content/>
            </#if>
            
        </div><!--
    --></div>
</#if>

<div class="grid"><!--
    --><div class="grid__item medium--four-twelfths large--three-twelfths">
        <@hst.include ref="side-filter"/>
    </div><!--
    --><div class="grid__item medium--eight-twelfths large--seven-twelfths">
        <@hst.include ref="results"/>
    </div><!--
--></div>

</div>

<@hst.headContribution category="footerScripts">
    <script src="<@hst.webfile path="/assets/scripts/filtered-list-page.js"/>" type="text/javascript"></script>
</@hst.headContribution>
<@hst.headContribution>
    <link rel="stylesheet" type="text/css" href="<@hst.webfile path="/assets/css/filters.css"/>"/>
</@hst.headContribution>

<#if index??>
    <@hst.headContribution category="pageTitle"><title>${index.title} - gov.scot</title></@hst.headContribution>

    <@hst.link var="canonicalitem" hippobean=index canonical=true />
    <@hst.headContribution>
        <link rel="canonical" href="${canonicalitem}"/>
    </@hst.headContribution>
    <#include "../common/canonical.ftl" />
</#if>
