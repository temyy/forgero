package com.sigmundgranaas.forgero.core.state;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.sigmundgranaas.forgero.core.condition.ConditionContainer;
import com.sigmundgranaas.forgero.core.condition.Conditional;
import com.sigmundgranaas.forgero.core.customdata.DataContainer;
import com.sigmundgranaas.forgero.core.property.Property;
import com.sigmundgranaas.forgero.core.property.PropertyContainer;
import com.sigmundgranaas.forgero.core.property.Target;
import com.sigmundgranaas.forgero.core.type.Type;
import com.sigmundgranaas.forgero.core.util.match.MatchContext;
import com.sigmundgranaas.forgero.core.util.match.Matchable;
import com.sigmundgranaas.forgero.core.util.match.NameMatch;
import org.jetbrains.annotations.NotNull;

public class ConditionedState implements State, Conditional<ConditionedState> {
	private final IdentifiableContainer id;
	private final ConditionContainer conditions;

	private final List<Property> properties;

	private final DataContainer customData;

	public ConditionedState(IdentifiableContainer id, ConditionContainer conditions, List<Property> properties, DataContainer customData) {
		this.id = id;
		this.conditions = conditions;
		this.properties = properties;
		this.customData = customData;
	}

	public static ConditionedState of(State state) {
		IdentifiableContainer id = new IdentifiableContainer(state.name(), state.nameSpace(), state.type());
		return new ConditionedState(id, Conditional.EMPTY, state.getRootProperties(), state.customData());
	}

	@Override
	public List<PropertyContainer> localConditions() {
		return conditions.localConditions();
	}

	@Override
	public ConditionedState applyCondition(PropertyContainer container) {
		return new ConditionedState(id, conditions.applyCondition(container), properties, customData);
	}

	@Override
	public ConditionedState removeCondition(String identifier) {
		return new ConditionedState(id, conditions.removeCondition(identifier), properties, customData);
	}

	@Override
	public String identifier() {
		return id.identifier();
	}

	@Override
	public @NotNull
	List<Property> getRootProperties() {
		return Stream.of(properties.stream().toList(), localConditions().stream()
						.map(PropertyContainer::getRootProperties)
						.flatMap(List::stream).toList())
				.flatMap(List::stream)
				.toList();
	}

	@Override
	public @NotNull List<Property> getRootProperties(Matchable target, MatchContext context) {
		return Stream.of(properties.stream().toList(), localConditions().stream()
						.map(state -> state.getRootProperties(target, context))
						.flatMap(List::stream).toList())
				.flatMap(List::stream)
				.toList();
	}

	@Override
	public String name() {
		return id.name();
	}

	@Override
	public String nameSpace() {
		return id.nameSpace();
	}

	@Override
	public Type type() {
		return id.type();
	}

	@Override
	public State strip() {
		return new SimpleState(name(), nameSpace(), type(), properties);
	}

	@Override
	public boolean test(Matchable match, MatchContext context) {
		if (match instanceof NameMatch matcher) {
			return matcher.name().equals(id.name());
		}
		return id.type().test(match, context);
	}

	@Override
	public DataContainer customData(Target target) {
		return customData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ConditionedState that = (ConditionedState) o;
		return Objects.equals(id, that.id) && Objects.equals(conditions, that.conditions) && Objects.equals(properties, that.properties) && Objects.equals(customData, that.customData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, conditions, customData);
	}
}
