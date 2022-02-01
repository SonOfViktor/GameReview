package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Payment;

import java.util.List;

/**
 * The interface provide methods to control business logic related with payments.
 */
public interface PaymentService {
    /**
     * Find all user's payments.
     *
     * @param content request content
     * @return list with payments
     * @throws ServiceException when DaoException occurred
     */
    List<Payment> findAllUserPayments(SessionRequestContent content) throws ServiceException;

    /**
     * Add user's payment to data-base.
     *
     * @param content request content
     * @return true if user's payment was added
     * @throws ServiceException when DaoException occurred
     */
    boolean addPayment(SessionRequestContent content) throws ServiceException;
}
