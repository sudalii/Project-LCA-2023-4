package org.openlca.app.editors.graphical_legacy;

import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openlca.app.rcp.images.Images;
import org.openlca.app.util.Labels;
import org.openlca.core.model.descriptors.Descriptor;

public final class GraphicalEditorInput implements IEditorInput {

	private Descriptor descriptor;

	public GraphicalEditorInput(Descriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GraphicalEditorInput) {
			var other = (GraphicalEditorInput) obj;
			return Objects.equals(this.descriptor, other.descriptor);
		}
		return false;
	}

	@Override
	public boolean exists() {
		return descriptor != null;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(final Class adapter) {
		return null;
	}

	public Descriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		if (descriptor == null)
			return null;
		return Images.descriptor(descriptor);
	}

	@Override
	public String getName() {
		if (descriptor == null)
			return "no content";
		return Labels.name(descriptor);
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		if (descriptor == null)
			return "no content";
		return descriptor.name;
	}

}
