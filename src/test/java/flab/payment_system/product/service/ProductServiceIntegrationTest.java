package flab.payment_system.product.service;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.product.dto.ProductDto;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.exception.ProductNotExistBadRequestException;
import flab.payment_system.domain.product.repository.ProductRepository;
import flab.payment_system.domain.product.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
