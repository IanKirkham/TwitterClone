package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.StatusesService;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.StatusesDAO;

public class StatusesServiceImpl implements StatusesService {
    @Override
    public StatusesResponse getStatuses(StatusesRequest request) {
        return getStatusesDAO().getStatuses(request);
    }

    StatusesDAO getStatusesDAO() {
        return new StatusesDAO();
    }
}
