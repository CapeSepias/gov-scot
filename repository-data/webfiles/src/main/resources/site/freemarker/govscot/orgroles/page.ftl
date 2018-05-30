<#include "../../include/imports.ftl">

<#if document??>
    <@hst.manageContent hippobean=document/>

    <div class="grid" id="page-content"><!--
    --><div class="grid__item medium--nine-twelfths push--medium--three-twelfths">

        <@hst.include ref="content"/>

    </div><!--
    --><div class="grid__item medium--three-twelfths pull--medium--nine-twelfths">
        <@hst.include ref="side-menu"/> 
    </div><!--
--></div>

<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->
<#elseif editMode>
  <div>
    <img src="<@hst.link path="/images/essentials/catalog-component-icons/simple-content.png" />"> Click to edit Content
  </div>
</#if>

<@hst.headContribution category="pageTitle"><title>${document.title} - gov.scot</title></@hst.headContribution>
