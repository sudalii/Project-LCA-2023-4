package org.openlca.app.wizards.io;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.openlca.app.M;
import org.openlca.app.db.Database;
import org.openlca.core.database.ImpactMethodDao;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.model.ImpactMethod;
import org.openlca.core.model.ModelType;
import org.openlca.core.model.Process;
import org.openlca.core.model.descriptors.RootDescriptor;
import org.openlca.io.ecospold1.output.EcoSpold1Export;
import org.openlca.io.ecospold1.output.ExportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wizard for exporting processes and impact methods to the EcoSpold01 format
 */
public class EcoSpold01ExportWizard extends Wizard implements IExportWizard {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private ModelSelectionPage modelPage;
	private Es1ExportConfigPage configPage;
	private final ModelType type;

	public EcoSpold01ExportWizard(ModelType type) {
		super();
		setNeedsProgressMonitor(true);
		this.type = type;
	}

	@Override
	public void addPages() {
		modelPage = ModelSelectionPage.forDirectory(type);
		addPage(modelPage);
		configPage = new Es1ExportConfigPage();
		addPage(configPage);
	}

	@Override
	public void init(final IWorkbench workbench,
			final IStructuredSelection selection) {
		setWindowTitle(M.ExportEcoSpold);
	}

	@Override
	public boolean performFinish() {
		boolean errorOccured = false;
		var models = modelPage.getSelectedModels();
		ExportConfig config = getConfig();
		try (EcoSpold1Export export = new EcoSpold1Export(
				modelPage.getExportDestination(), config)) {
			getContainer().run(true, true, (monitor) -> {
				int size = models.size();
				monitor.beginTask(M.ExportingProcesses, size + 1);
				monitor.subTask(M.CreatingEcoSpoldFolder);
				monitor.worked(1);
				doExport(models, monitor, export);
				monitor.done();
			});
		} catch (Exception e) {
			log.error("Perform finish failed", e);
			errorOccured = true;
		}
		return !errorOccured;
	}

	private ExportConfig getConfig() {
		if (configPage == null)
			return ExportConfig.getDefault();
		else
			return configPage.getConfig();
	}

	private void doExport(List<RootDescriptor> models,
			IProgressMonitor monitor, EcoSpold1Export export)
			throws InterruptedException {
		ProcessDao pDao = new ProcessDao(Database.get());
		ImpactMethodDao mDao = new ImpactMethodDao(Database.get());
		try {
			for (var d : models) {
				if (monitor.isCanceled())
					break;
				monitor.subTask(d.name);
				if (type == ModelType.PROCESS) {
					Process process = pDao.getForId(d.id);
					export.export(process);
				} else if (type == ModelType.IMPACT_METHOD) {
					ImpactMethod method = mDao.getForId(d.id);
					export.export(method);
				}
				monitor.worked(1);
			}
		} catch (Exception e) {
			log.error("Export failed", e);
			throw new InterruptedException(e.getMessage());
		}
	}
}
