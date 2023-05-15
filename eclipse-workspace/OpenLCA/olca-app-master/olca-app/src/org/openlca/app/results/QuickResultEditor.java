package org.openlca.app.results;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.openlca.app.db.Cache;
import org.openlca.app.preferences.FeatureFlag;
import org.openlca.app.results.contributions.TagResultPage;
import org.openlca.app.results.contributions.locations.LocationPage;
import org.openlca.app.results.grouping.GroupPage;
import org.openlca.core.math.data_quality.DQResult;
import org.openlca.core.model.CalculationSetup;
import org.openlca.core.results.ContributionResult;
import org.openlca.core.results.ResultItemView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickResultEditor extends ResultEditor<ContributionResult> {

	public static String ID = "QuickResultEditor";
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(IEditorSite site, IEditorInput iInput)
			throws PartInitException {
		super.init(site, iInput);
		try {
			
			
			var input = (ResultEditorInput) iInput;
			setup = Cache.getAppCache().remove(input.setupKey,
					CalculationSetup.class);
			result = Cache.getAppCache().remove(
					input.resultKey, ContributionResult.class);
			resultItems = ResultItemView.of(result);
			Sort.sort(resultItems);
			if (input.dqResultKey != null) {
				dqResult = Cache.getAppCache().remove(
					input.dqResultKey, DQResult.class);
			}
		} catch (Exception e) {
			log.error("failed to load inventory result", e);
			throw new PartInitException(
					"failed to load inventory result", e);
		}
	}

	@Override
	protected void addPages() {
		try {
			long beforeTime = 0L;
			long afterTime = 0L;
			
			beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
			System.out.println("\nQuickResult-beforeTime : "+beforeTime);
			
			addPage(new InfoPage(this));
			addPage(new InventoryPage(this));
			if (result.hasImpacts()) {
				addPage(new TotalImpactResultPage(this));
			}
			if (result.hasImpacts() && setup.nwSet() != null) {
				addPage(new NwResultPage(this, result, setup));
			}
			addPage(new LocationPage(this, result, setup));
			addPage(new GroupPage(this, result, setup));
			if (result.hasImpacts()) {
				addPage(new ImpactChecksPage(this));
			}
			if (FeatureFlag.TAG_RESULTS.isEnabled()) {
				addPage(new TagResultPage(this));
			}
			afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
			System.out.println("\nQuickResult-afterTime : "+afterTime);

			long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
			System.out.println("\n\n시간차이 : "+secDiffTime +"ms");
			
		} catch (Exception e) {
			log.error("failed to add pages", e);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
		SaveResultDialog.open(this);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void setFocus() {
	}
}
