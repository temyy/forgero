package com.sigmundgranaas.forgero.core.model;

import java.util.Optional;

import com.sigmundgranaas.forgero.core.model.match.predicate.IdPredicate;
import com.sigmundgranaas.forgero.core.model.match.predicate.ModelPredicate;
import com.sigmundgranaas.forgero.core.texture.utils.Offset;
import com.sigmundgranaas.forgero.core.util.match.MatchContext;
import com.sigmundgranaas.forgero.core.util.match.Matchable;
import org.jetbrains.annotations.NotNull;

public interface ModelMatcher extends Comparable<ModelMatcher> {
	ModelMatcher EMPTY_TRUE = new ModelMatcher() {
		@Override
		public int compareTo(@NotNull ModelMatcher o) {
			return comparator(this, o);
		}

		@Override
		public boolean match(Matchable state, MatchContext context) {
			return true;
		}

		@Override
		public Optional<ModelTemplate> get(Matchable state, ModelProvider provider, MatchContext context) {
			return Optional.of(EMPTY_TEMPLATE);
		}
	};

	ModelTemplate EMPTY_TEMPLATE = new ModelTemplate() {
		@Override
		public int order() {
			return 0;
		}

		@Override
		public Optional<Offset> getOffset() {
			return Optional.empty();
		}

		@Override
		public <T> T convert(Converter<T, ModelTemplate> converter) {
			return converter.convert(this);
		}
	};

	ModelMatcher EMPTY = new ModelMatcher() {
		@Override
		public int compareTo(@NotNull ModelMatcher o) {
			return comparator(this, o);
		}

		@Override
		public boolean match(Matchable state, MatchContext context) {
			return false;
		}

		@Override
		public Optional<ModelTemplate> get(Matchable state, ModelProvider provider, MatchContext context) {
			return Optional.empty();
		}
	};

	static int comparator(ModelMatcher match1, ModelMatcher match2) {
		if (match1 instanceof ModelMatchPairing entry1 && match2 instanceof ModelMatchPairing entry2) {
			var matcher1 = entry1.match();
			var matcher2 = entry2.match();
			var match1Identifier = matcher1.getPredicates().stream().anyMatch(match -> match instanceof IdPredicate);
			var match2Identifier = matcher2.getPredicates().stream().anyMatch(match -> match instanceof IdPredicate);

			var match1Model = matcher1.getPredicates().stream().anyMatch(match -> match instanceof ModelPredicate);
			var match2Model = matcher2.getPredicates().stream().anyMatch(match -> match instanceof ModelPredicate);


			//If one references an identifier, it is preferred
			if (match1Identifier && !match2Identifier) {
				return -1;
			} else if (match2Identifier && !match1Identifier) {
				return 1;
			}

			//If one references a model, it is preferred
			if (match1Model && !match2Model) {
				return -1;
			} else if (match2Model && !match1Model) {
				return 1;
			}

			//If neither references an identifier, the one with the most criteria is preferred
			return Integer.compare(matcher2.getPredicates().size(), matcher1.getPredicates().size());
		}


		return 0;
	}

	boolean match(Matchable state, MatchContext context);

	Optional<ModelTemplate> get(Matchable state, ModelProvider provider, MatchContext context);

	@Override
	default int compareTo(@NotNull ModelMatcher o) {
		return comparator(this, o);
	}
}
