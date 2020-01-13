package fr.femtost.sbs.alteration.core.basestation.engine;

import fr.femtost.sbs.alteration.core.basestation.message.BaseStationMessage;
import fr.femtost.sbs.alteration.core.engine.ActionEngine;
import fr.femtost.sbs.alteration.core.engine.Message;
import fr.femtost.sbs.alteration.core.scenario.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static fr.femtost.sbs.alteration.core.basestation.BaseStationParser.createBstMessage;

public abstract class BaseStationActionEngine extends ActionEngine {

    BaseStationActionEngine(final Recording recording, final Action action) {
        super(recording, action);
    }

    public static File run(final Recording recording, final Action action) throws UnknownActionException, IOException, UnknownScopeException, UnknownCharacteristicException {
        return new Action.ActionTypeSwitch<BaseStationActionEngine>() {

            @Override
            public BaseStationActionEngine visitAlteration() {
                return new BaseStationAlterationEngine(recording, action);
            }

            @Override
            public BaseStationActionEngine visitDeletion() {
                return new BaseStationDeletionEngine(recording, action);
            }

            @Override
            public BaseStationActionEngine visitSaturation() {
                return new BaseStationSaturationEngine(recording, action);
            }

            @Override
            public BaseStationActionEngine visitDelay() {
                return new BaseStationDelayEngine(recording, action);
            }

            @Override
            public BaseStationActionEngine visitReplay() {
                return new BaseStationReplayEngine(recording, action);
            }

            @Override
            public BaseStationActionEngine visitTrajectoryModification() {
                return new BaseStationTrajectoryModificationEngine(recording, action);
            }
        }.doSwitch(action.getActionType()).processAction();
    }

    static boolean isMessageInScope(final BaseStationMessage message, final Scope scope, final long firstDate) throws UnknownScopeException {
        return new Scope.ScopeTypeSwitch<Boolean>() {

            final long relativeDate = message.getTimestampGenerated() - firstDate;

            @Override
            public Boolean visitGeoTimeWindow() {
                // TODO: manage polygons
                return scope.getLowerBound() <= relativeDate && relativeDate <= scope.getUpperBound();
            }

            @Override
            public Boolean visitTimeWindow() {
                return scope.getLowerBound() <= relativeDate && relativeDate <= scope.getUpperBound();
            }
        }.doSwitch(scope.getType());
    }

    @Override
    protected Optional<Message> messagePreprocessing(final String message) {
        return createBstMessage(message);
    }

    @Override
    protected String getExtension() {
        return ".bst";
    }
}