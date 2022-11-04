package com.trilogyed.gamestoreinvoicing.repository;

import com.trilogyed.gamestoreinvoicing.model.Invoice;
import com.trilogyed.gamestoreinvoicing.model.ProcessingFee;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import com.trilogyed.gamestoreinvoicing.model.Tax;
import com.trilogyed.gamestoreinvoicing.util.feign.GameStoreCatalogClient;
import com.trilogyed.gamestoreinvoicing.viewModel.TShirtViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest

public class InvoiceRepositoryTest {

    @Autowired
    InvoiceRepository invoiceRepository;


    @Before
    public void setUp() throws Exception {
        invoiceRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteInvoice() {
        // Sample testing code in class with Dan on Thursday, 11/03/2022
        // Build an invoice
        Invoice invoice = new Invoice();
        invoice.setUnitPrice(new BigDecimal("5.01"));
        invoice.setCity("Houston");
        invoice.setState("WA");
        invoice.setProcessingFee(new BigDecimal("1000"));
        invoice.setItemType("Chocolate Bar");
        invoice.setSubtotal(new BigDecimal("44.41"));
        invoice.setQuantity(3);
        invoice.setTax(new BigDecimal("1.11"));
        invoice.setTotal(new BigDecimal("22.22"));
        invoice.setZipcode("95127");
        invoice.setName("Bill Smith");
        // Save to database
        invoice = invoiceRepository.save(invoice);
        // get it back out of the database
        Invoice invoice2 = invoiceRepository.findById(invoice.getId()).get();
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(invoice, invoice2);
        // delete it
        invoiceRepository.deleteById(invoice.getId());
        //go try to get it again
        Optional<Invoice> invoice3 = invoiceRepository.findById(invoice.getId());
        // confirm that it's gone
        assertEquals(false,invoice3.isPresent());
        //sample test - end here



    }

    @Test
    public void shouldFindByName() {

        // Sample testing code in class with Dan on Thursday, 11/03/2022
        // Build an invoice
        Invoice invoice = new Invoice();
        invoice.setUnitPrice(new BigDecimal("5.01"));
        invoice.setCity("Houston");
        invoice.setState("WA");
        invoice.setProcessingFee(new BigDecimal("1000"));
        invoice.setItemType("Chocolate Bar");
        invoice.setSubtotal(new BigDecimal("44.41"));
        invoice.setQuantity(3);
        invoice.setTax(new BigDecimal("1.11"));
        invoice.setTotal(new BigDecimal("22.22"));
        invoice.setZipcode("95127");
        invoice.setName("Bill Smith");
        // Save to database
        invoice = invoiceRepository.save(invoice);

        List<Invoice> foundNoinvoice = invoiceRepository.findByName("invalidValue");

        List<Invoice> foundOneinvoice = invoiceRepository.findByName(invoice.getName());

        //Assert
        assertEquals(foundOneinvoice.size(),1);

        //Assert
        assertEquals(foundNoinvoice.size(),0);
    }
}