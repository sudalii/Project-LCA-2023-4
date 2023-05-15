package org.openlca.app.wizards;

import java.util.UUID;

import org.openlca.app.M;
import org.openlca.app.db.Database;
import org.openlca.core.database.CurrencyDao;
import org.openlca.core.model.Currency;
import org.openlca.core.model.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyWizard extends AbstractWizard<Currency> {

	@Override
	protected String getTitle() {
		return M.NewCurrency;
	}

	@Override
	protected AbstractWizardPage<Currency> createPage() {
		return new Page();
	}

	@Override
	protected ModelType getModelType() {
		return ModelType.CURRENCY;
	}

	private static class Page extends AbstractWizardPage<Currency> {

		Page() {
			super("CurrencyWizardPage");
			setTitle(M.NewCurrency);
			setPageComplete(false);
		}

		@Override
		public Currency createModel() {
			Currency c = new Currency();
			c.refId = UUID.randomUUID().toString();
			c.name = getModelName();
			c.description = getModelDescription();
			c.conversionFactor = 1.0;
			c.code = getModelName();
			var ref = getRefCurrency();
			c.referenceCurrency = ref != null
				? ref
				: c;
			return c;
		}

		private Currency getRefCurrency() {
			try {
				CurrencyDao dao = new CurrencyDao(Database.get());
				return dao.getReferenceCurrency();
			} catch (Exception e) {
				Logger log = LoggerFactory.getLogger(getClass());
				log.error("failed to get the reference currency", e);
				return null;
			}
		}
	}

}
