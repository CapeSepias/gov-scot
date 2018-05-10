package scot.gov.www.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.HstResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.gov.www.beans.Publication;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

/**
 * Redirect isbn urls
 */
public class PublicationsIsbnRedirectComponent extends BaseHstComponent {

    private static final Logger LOG = LoggerFactory.getLogger(PublicationsIsbnRedirectComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        HippoBean bean = findByIsbn(request);
        if (bean == null) {
            response.setStatus(404);
            return;
        }
        HstRequestContext context = request.getRequestContext();
        final HstLink link = context.getHstLinkCreator().create(bean, context);
        HstResponseUtils.sendPermanentRedirect(request, response, link.getPath());
    }

    private HippoBean findByIsbn(HstRequest request) {
        String isbn = isbn(request);
        HstQuery query = HstQueryBuilder
                .create(request.getRequestContext().getSiteContentBaseBean())
                .ofTypes(Publication.class)
                .where(constraint("govscot:isbn").equalTo(isbn))
                .build();
        return executeQuery(query, isbn);
    }

    private String isbn(HstRequest request) {
        String [] pathElements = request.getPathInfo().split("/");
        return pathElements[pathElements.length - 1];
    }

    private HippoBean executeQuery(HstQuery query, String isbn) {
        try {
            HstQueryResult result = query.execute();
            if (result.getTotalSize() == 0) {
                LOG.warn("ISBN not found: {}", isbn);
                return null;
            }

            if (result.getTotalSize() > 1) {
                LOG.warn("Multiple publications with this ISBN : {}, will use first", isbn);
            }

            return result.getHippoBeans().nextHippoBean();
        } catch (QueryException e) {
            LOG.error("Failed to get publication by ISBN {}", isbn, e);
            return null;
        }
    }
}