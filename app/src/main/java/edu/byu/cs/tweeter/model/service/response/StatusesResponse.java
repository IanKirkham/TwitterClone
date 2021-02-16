package edu.byu.cs.tweeter.model.service.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;

/**
 * A paged response for a {@link StatusesRequest}.
 */
public class StatusesResponse extends PagedResponse {

    private List<Status> statuses;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public StatusesResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param statuses the statuses that form the requested Feed/Story.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public StatusesResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    /**
     * Returns the statuses for the corresponding request.
     *
     * @return the statuses.
     */
    public List<Status> getStatuses() {
        return statuses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusesResponse that = (StatusesResponse) o;
        return statuses.equals(that.statuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses);
    }
}
