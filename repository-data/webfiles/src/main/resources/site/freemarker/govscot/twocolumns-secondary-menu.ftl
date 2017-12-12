<#include "../include/imports.ftl">

two columns secondary
<#if menu??>
<aside class="page-group page-group--section-nav page-grouppage-subnav--tablet">
    <ul class="page-group__list">
        <#if menu.siteMenuItems??>
            <#list menu.siteMenuItems as item>
                <#if item.selected || item.expanded>
                    <li class="page-group__item page-group__item--level-0">

                        <#if item.selected>
                        <span href="<@hst.link link=item.hstLink/>" class="page-group__link  page-group__link--level-0  page-group__link--selected">
                            ${item.name?html}
                        </span>
                        <#else>
                        <a href="<@hst.link link=item.hstLink/>" class="page-group__link  page-group__link--level-0">
                            ${item.name?html}
                        </a>
                        </#if>

                        <#if item.childMenuItems??>
                            <ul class="page-group__list">
                                <#list item.childMenuItems as item>
                                    <li class="page-group__item page-group__item--level-1">
                                        <#if item.selected>
                                        <span href="<@hst.link link=item.hstLink/>" class="page-group__link  page-group__link--level-1  page-group__link--selected">
                                            ${item.name?html}
                                        </span>
                                        <#else>
                                        <a href="<@hst.link link=item.hstLink/>" class="page-group__link  page-group__link--level-1">
                                            ${item.name?html}
                                        </a>
                                        </#if>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                    </li>
                </#if>
            </#list>
        </#if>
    </ul>
</aside>
</#if>
