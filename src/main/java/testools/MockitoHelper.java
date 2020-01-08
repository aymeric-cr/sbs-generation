package testools;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;

import static org.powermock.api.mockito.PowerMockito.doAnswer;

public final class MockitoHelper {

    private MockitoHelper() {
    }

    public static PowerMockitoStubber mockVoid(final Runnable runnable) {
        return doAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                runnable.run();
                return null;
            }
        });
    }
}
