package scot.gov.www.linkprocessors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.linking.HstLinkProcessorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

public class PublicationLinkProcessorNameLikeImpl extends HstLinkProcessorTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(PublicationLinkProcessor.class);

    public static final String PUBLICATIONS = "publications/";

    @Override
    protected HstLink doPostProcess(HstLink link) {
        if (isPublicationsFullLink(link)) {
            // remove the type, year and month...
            String [] newElements = ArrayUtils.removeElements(link.getPathElements(),
                    link.getPathElements()[1],
                    link.getPathElements()[2],
                    link.getPathElements()[3]);
            link.setPath(String.join("/", newElements));
        }
        return link;
    }

    private boolean isPublicationsFullLink(HstLink link) {
        // should match on any publication or publication page link.
        return link.getPath().startsWith(PUBLICATIONS) && link.getPathElements().length >= 5;
    }

    @Override
    protected HstLink doPreProcess(HstLink link) {
        if (isPublicationsSlugLink(link)) {
            return preProcessPublicationsLink(link);
        }
        return link;
    }

    private boolean isPublicationsSlugLink(HstLink link) {
        // match any slug style link for a publication or page
        return link.getPath().startsWith(PUBLICATIONS) && link.getPathElements().length >= 2;
    }

    private HstLink preProcessPublicationsLink(HstLink link) {

        try {
            String slug = link.getPathElements()[1];
            String [] remaining = ArrayUtils.removeElements(link.getPathElements(),
                    link.getPathElements()[0],
                    link.getPathElements()[1]);
            Node handle = getHandleBySlug(slug);
            if(handle == null) {
                link.setNotFound(true);
                link.setPath("/pagenotfound");
                return link;
            }

            String pubPath = StringUtils.substringAfter(handle.getPath(), PUBLICATIONS);

            String newPath = null;
            if (remaining.length == 0) {
                // this is a link to the publication, include the /index part.
                newPath = String.format("publications/%s", pubPath);
            } else {
                // this is a link to a page, remove the index and add on the pages part
                newPath = String.format("publications/%s/%s", StringUtils.substringBefore(pubPath, "/index"), String.join("/", remaining));
            }
            link.setPath(newPath);
            return link;
        } catch (RepositoryException e) {
            LOG.warn("Exception trying to process link: {}", link.getPath(), e);
            return link;
        }
    }

    private Node getHandleBySlug(String slug) throws RepositoryException {
        HstRequestContext req = RequestContextProvider.get();
        Session session = req.getSession();
        String sql = String.format("SELECT * FROM hippostd:folder WHERE jcr:name LIKE '%s'", slug);
        QueryResult result = session.getWorkspace().getQueryManager().createQuery(sql, Query.SQL).execute();
        if (result.getNodes().getSize() == 0) {
            return null;
        }



        // find the index in this folder
        Node folder = findPublicationFolder(result);

        if (folder == null) {
            return null;
        }

        NodeIterator nodeIterator = folder.getNodes();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();
            if ("index".equals(node.getName())) {
                return node;
            }
        }

        return null;
    }

    private Node findPublicationFolder(QueryResult result) throws RepositoryException {
        NodeIterator nodeIterator = result.getNodes();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();
            if (node.getPath().startsWith("/content/documents/govscot/publications")) {
                return node;
            }
        }
        return null;
    }

}