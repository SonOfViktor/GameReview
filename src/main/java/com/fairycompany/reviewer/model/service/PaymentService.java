package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAllPayments(SessionRequestContent content) throws ServiceException;

    boolean addPayment(SessionRequestContent content) throws ServiceException;
}
