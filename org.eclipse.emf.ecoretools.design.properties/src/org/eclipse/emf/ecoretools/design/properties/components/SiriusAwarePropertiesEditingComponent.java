package org.eclipse.emf.ecoretools.design.properties.components;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.eef.runtime.api.component.IPropertiesEditionComponent;
import org.eclipse.emf.eef.runtime.api.notify.IPropertiesEditionComponentListener;
import org.eclipse.emf.eef.runtime.context.ExtendedPropertiesEditingContext;
import org.eclipse.emf.eef.runtime.context.PropertiesEditingContext;
import org.eclipse.emf.eef.runtime.impl.components.SinglePartPropertiesEditingComponent;
import org.eclipse.emf.eef.runtime.impl.services.PropertiesEditionComponentListenerProviderService;
import org.eclipse.sirius.business.api.session.SessionManager;

/**
 * Override of the {@link SinglePartPropertiesEditingComponent} preventing the
 * excessive removing of EEF SemanticListener.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public abstract class SiriusAwarePropertiesEditingComponent extends SinglePartPropertiesEditingComponent {
    /**
     * Create a new properties editing component which leverages the Sirius apis
     * to be more efficient.
     * 
     * @param editingContext
     *            the current editing context.
     * @param semanticObject
     *            the semantic object.
     * @param editingMode
     *            the editing mode.
     */
    public SiriusAwarePropertiesEditingComponent(PropertiesEditingContext editingContext, EObject semanticObject, String editingMode) {
        super(editingContext, semanticObject, editingMode);
    }

    /**
     * {@inheritDoc}
     * 
     * @see IPropertiesEditionComponent#deactivate()
     */
    public void deactivate() {
        if (semanticAdapter != null) {
            PropertiesEditingContext editingContext = getEditingContext();
            if (editingContext instanceof ExtendedPropertiesEditingContext && ((ExtendedPropertiesEditingContext) editingContext).canReachResourceSetAdapter()) {
                if (SessionManager.INSTANCE.getSession(getEditingContext().getEObject()) != null) {
                    ((ExtendedPropertiesEditingContext) editingContext).getResourceSetAdapter().unregisterEditingSemanticListener(semanticAdapter);
                } else {
                    ((ExtendedPropertiesEditingContext) editingContext).getResourceSetAdapter().removeEditingSemanticListener(semanticAdapter);
                }
            }
        }
        for (IPropertiesEditionComponentListener listener : PropertiesEditionComponentListenerProviderService.getInstance().getListeners()) {
            listener.deactivate(this);
        }
    }

}
