package com.trilogyed.gamestoreinvoicing.service;


import com.trilogyed.gamestoreinvoicing.model.*;
import com.trilogyed.gamestoreinvoicing.repository.InvoiceRepository;
import com.trilogyed.gamestoreinvoicing.repository.ProcessingFeeRepository;
import com.trilogyed.gamestoreinvoicing.repository.TaxRepository;
import com.trilogyed.gamestoreinvoicing.util.feign.GameStoreCatalogClient;
import com.trilogyed.gamestoreinvoicing.viewModel.InvoiceViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.TShirtViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameStoreServiceLayerTest {


    private GameStoreCatalogClient client;
    private InvoiceRepository invoiceRepository;
    private TaxRepository taxRepository;
    private ProcessingFeeRepository processingFeeRepository;

    private GameStoreServiceLayer service;


    @Before
    public void setUp() throws Exception {
        setUpClientMock();
        setUpInvoiceRepositoryMock();
        setUpProcessingFeeRepositoryMock();
        setUpTaxRepositoryMock();

        service = new GameStoreServiceLayer(client, invoiceRepository,
                taxRepository, processingFeeRepository);
    }

//    Testing Invoice Operations...
    @Test
    public void shouldCreateFindInvoice() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test
    public void shouldFindAllInvoices(){
        InvoiceViewModel savedInvoice1 = new InvoiceViewModel();
        savedInvoice1.setName("Sandy Beach");
        savedInvoice1.setStreet("123 Main St");
        savedInvoice1.setCity("any City");
        savedInvoice1.setState("NY");
        savedInvoice1.setZipcode("10016");
        savedInvoice1.setItemType("T-Shirt");
        savedInvoice1.setItemId(12);//pretending item exists with this id...
        savedInvoice1.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice1.setQuantity(2);
        savedInvoice1.setSubtotal(savedInvoice1.getUnitPrice().multiply(new BigDecimal(savedInvoice1.getQuantity())));
        savedInvoice1.setTax(savedInvoice1.getSubtotal().multiply(new BigDecimal("0.06")));
        savedInvoice1.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice1.setTotal(savedInvoice1.getSubtotal().add(savedInvoice1.getTax()).add(savedInvoice1.getProcessingFee()));
        savedInvoice1.setId(22);

        InvoiceViewModel savedInvoice2 = new InvoiceViewModel();
        savedInvoice2.setName("Rob Bank");
        savedInvoice2.setStreet("888 Main St");
        savedInvoice2.setCity("any town");
        savedInvoice2.setState("NJ");
        savedInvoice2.setZipcode("08234");
        savedInvoice2.setItemType("Console");
        savedInvoice2.setItemId(120);//pretending item exists with this id...
        savedInvoice2.setUnitPrice(new BigDecimal("129.50"));//pretending item exists with this price...
        savedInvoice2.setQuantity(1);
        savedInvoice2.setSubtotal(savedInvoice2.getUnitPrice().multiply(new BigDecimal(savedInvoice2.getQuantity())));
        savedInvoice2.setTax(savedInvoice2.getSubtotal().multiply(new BigDecimal("0.08")));
        savedInvoice2.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice2.setTotal(savedInvoice2.getSubtotal().add(savedInvoice2.getTax()).add(savedInvoice2.getProcessingFee()));
        savedInvoice2.setId(12);

        InvoiceViewModel savedInvoice3 = new InvoiceViewModel();
        savedInvoice3.setName("Sandy Beach");
        savedInvoice3.setStreet("123 Broad St");
        savedInvoice3.setCity("any where");
        savedInvoice3.setState("CA");
        savedInvoice3.setZipcode("90016");
        savedInvoice3.setItemType("Game");
        savedInvoice3.setItemId(19);//pretending item exists with this id...
        savedInvoice3.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice3.setQuantity(4);
        savedInvoice3.setSubtotal(savedInvoice3.getUnitPrice().multiply(new BigDecimal(savedInvoice3.getQuantity())));
        savedInvoice3.setTax(savedInvoice3.getSubtotal().multiply(new BigDecimal("0.09")));
        savedInvoice3.setProcessingFee(BigDecimal.ZERO);
        savedInvoice3.setTotal(savedInvoice3.getSubtotal().add(savedInvoice3.getTax()).add(savedInvoice3.getProcessingFee()));
        savedInvoice3.setId(73);

        List<InvoiceViewModel> currInvoices = new ArrayList<>();
        currInvoices.add(savedInvoice1);
        currInvoices.add(savedInvoice2);
        currInvoices.add(savedInvoice3);

        List<InvoiceViewModel> foundAllInvoices = service.getAllInvoices();

        assertEquals(currInvoices.size(), foundAllInvoices.size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithBadState() {
        TShirtViewModel tShirt = new TShirtViewModel();
        tShirt.setId(99);
        tShirt.setSize("Small");
        tShirt.setColor("Red");
        tShirt.setDescription("sleeveless");
        tShirt.setPrice(new BigDecimal("400"));
        tShirt.setQuantity(30);

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NY");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(99);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithBadItemType() {


        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("Bad Item Type");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithNoInventory() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(6);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateInvoiceInvalidItem() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("nothing");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateInvoiceInvalidQuantity() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(0);

        invoiceViewModel = service.createInvoice(invoiceViewModel);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenCreateInvoiceInvalidInvoiceMV() {

        InvoiceViewModel invoiceViewModel = null;

        invoiceViewModel = service.createInvoice(invoiceViewModel);
    }


    private void setUpClientMock(){
        client = mock(GameStoreCatalogClient.class);
        //Console
        List<Console> allConsoles = new ArrayList<>();
        List<Console> consoleByManufacturer = new ArrayList<>();

        Console newConsole1 = new Console();
        newConsole1.setModel("Playstation 5");
        newConsole1.setManufacturer("Sony");
        newConsole1.setMemoryAmount("1 TB");
        newConsole1.setProcessor("Intel I9");
        newConsole1.setPrice(new BigDecimal("599.99"));
        newConsole1.setQuantity(5);

        Console savedConsole1 = new Console();
        savedConsole1.setId(40);
        savedConsole1.setModel("Playstation 5");
        savedConsole1.setManufacturer("Sony");
        savedConsole1.setMemoryAmount("1 TB");
        savedConsole1.setProcessor("Intel I9");
        savedConsole1.setPrice(new BigDecimal("599.99"));
        savedConsole1.setQuantity(5);

        consoleByManufacturer.add(savedConsole1);
        allConsoles.add(savedConsole1);

        Console newConsole2 = new Console();
        newConsole2.setModel("Xbox");
        newConsole2.setManufacturer("Microsoft");
        newConsole2.setMemoryAmount("1 TB");
        newConsole2.setProcessor("Intel I9");
        newConsole2.setPrice(new BigDecimal("499.99"));
        newConsole2.setQuantity(5);

        Console savedConsole2 = new Console();
        savedConsole2.setId(20);
        savedConsole2.setModel("Xbox");
        savedConsole2.setManufacturer("Microsoft");
        savedConsole2.setMemoryAmount("1 TB");
        savedConsole2.setProcessor("Intel I9");
        savedConsole2.setPrice(new BigDecimal("499.99"));
        savedConsole2.setQuantity(5);

        consoleByManufacturer.add(savedConsole2);
        allConsoles.add(savedConsole2);

        Console newConsole3 = new Console();
        newConsole3.setModel("Nintendo Switch");
        newConsole3.setManufacturer("Nintendo");
        newConsole3.setMemoryAmount("1 TB");
        newConsole3.setProcessor("I7");
        newConsole3.setPrice(new BigDecimal("399.99"));
        newConsole3.setQuantity(5);

        Console savedConsole3 = new Console();
        savedConsole3.setId(30);
        savedConsole3.setModel("Nintendo Switch");
        savedConsole3.setManufacturer("Nintendo");
        savedConsole3.setMemoryAmount("1 TB");
        savedConsole3.setProcessor("I7");
        savedConsole3.setPrice(new BigDecimal("399.99"));
        savedConsole3.setQuantity(5);

        allConsoles.add(savedConsole3);

        doReturn(savedConsole1).when(client).createConsole(newConsole1);
        doReturn(savedConsole2).when(client).createConsole(newConsole2);
        doReturn(savedConsole3).when(client).createConsole(newConsole3);
        doReturn(savedConsole1).when(client).getConsole(40);
        doReturn(consoleByManufacturer).when(client).getConsoleByManufacturer("Sony");
        doReturn(allConsoles).when(client).getAllConsoles();

        List<Game> gamesByEsrb = new ArrayList<>();
        List<Game> gamesByTitle = new ArrayList<>();
        List<Game> gamesByStudio = new ArrayList<>();
        List<Game> allGames = new ArrayList<>();

        //No ID in this "game" object
        Game newGame1 = new Game();
        newGame1.setTitle("Saint Kotar");
        newGame1.setEsrbRating("M 17+");
        newGame1.setDescription("A horror game");
        newGame1.setPrice(new BigDecimal("34.99"));
        newGame1.setStudio("Red Martyr Entertainment");
        newGame1.setQuantity(5);

        Game savedGame1 = new Game();
        savedGame1.setId(32);
        savedGame1.setTitle("Saint Kotar");
        savedGame1.setEsrbRating("M 17+");
        savedGame1.setDescription("A horror game");
        savedGame1.setPrice(new BigDecimal("34.99"));
        savedGame1.setStudio("Red Martyr Entertainment");
        savedGame1.setQuantity(10);
        gamesByEsrb.add(savedGame1);
        gamesByTitle.add(savedGame1);
        allGames.add(savedGame1);

        Game newGame2 = new Game();
        newGame2.setTitle("Overwatch 2");
        newGame2.setEsrbRating("T");
        newGame2.setDescription("First-person shooter");
        newGame2.setPrice(new BigDecimal("19.99"));
        newGame2.setStudio("Blizzard Entertainmen");
        newGame2.setQuantity(10);

        Game savedGame2 = new Game();
        savedGame2.setId(20);
        savedGame2.setTitle("Overwatch 2");
        savedGame2.setEsrbRating("T");
        savedGame2.setDescription("First-person shooter");
        savedGame2.setPrice(new BigDecimal("19.99"));
        savedGame2.setStudio("Blizzard Entertainmen");
        savedGame2.setQuantity(10);
        gamesByEsrb.add(savedGame2);
        gamesByStudio.add(savedGame2);
        allGames.add(savedGame2);

        Game newGame3 = new Game();
        newGame3.setTitle("A Plague Tale: Requiem");
        newGame3.setEsrbRating("M 17+");
        newGame3.setDescription("Adventure game");
        newGame3.setPrice(new BigDecimal("59.99"));
        newGame3.setStudio("Asobo Studio");
        newGame3.setQuantity(5);

        Game savedGame3 = new Game();
        savedGame3.setId(30);
        savedGame3.setTitle("A Plague Tale: Requiem");
        savedGame3.setEsrbRating("M 17+");
        savedGame3.setDescription("Adventure game");
        savedGame3.setPrice(new BigDecimal("59.99"));
        savedGame3.setStudio("Asobo Studios");
        savedGame3.setQuantity(5);
        gamesByTitle.add(savedGame3);
        gamesByStudio.add(savedGame3);
        allGames.add(savedGame3);

        doReturn(savedGame1).when(client).createGame(newGame1);
        doReturn(savedGame2).when(client).getGame(20);
        doReturn(savedGame1).when(client).getGame(32);
        doReturn(savedGame3).when(client).getGame(30);
        doReturn(savedGame2).when(client).createGame(newGame2);
        doReturn(savedGame3).when(client).createGame(newGame3);

        doReturn(gamesByEsrb).when(client).getGameByEsrbRating("M 17+");
        doReturn(gamesByStudio).when(client).getGameByStudio("Red Martyr Entertainment");
        doReturn(gamesByTitle).when(client).getGameByTitle("Saint Kotar");
        doReturn(allGames).when(client).getAllGames();

        List<TShirt> tShirtsByColor = new ArrayList<>();
        List<TShirt> tShirtsBySize = new ArrayList<>();
        List<TShirt> allTtShirts = new ArrayList<>();

        TShirt newTShirt1 = new TShirt();
        newTShirt1.setSize("Small");
        newTShirt1.setColor("Green");
        newTShirt1.setDescription("T-Shirt");
        newTShirt1.setPrice(new BigDecimal("10.99"));
        newTShirt1.setQuantity(5);

        TShirt savedTShirt1 = new TShirt();
        savedTShirt1.setId(54); // set up for mocking the Id of 54
        savedTShirt1.setSize("Small");
        savedTShirt1.setColor("Green");
        savedTShirt1.setDescription("T-Shirt");
        savedTShirt1.setPrice(new BigDecimal("19.99")); // set up for mocking the price of $19.99
        savedTShirt1.setQuantity(5);

        tShirtsByColor.add(savedTShirt1);
        tShirtsBySize.add(savedTShirt1);
        allTtShirts.add(savedTShirt1);

        TShirt newTShirt2 = new TShirt();
        newTShirt2.setSize("Medium");
        newTShirt2.setColor("Red");
        newTShirt2.setDescription("T-Shirt");
        newTShirt2.setPrice(new BigDecimal("10.99"));
        newTShirt2.setQuantity(5);

        TShirt savedTShirt2 = new TShirt();
        savedTShirt2.setId(20);
        savedTShirt2.setSize("Medium");
        savedTShirt2.setColor("Red");
        savedTShirt2.setDescription("T-Shirt");
        savedTShirt2.setPrice(new BigDecimal("10.99"));
        savedTShirt2.setQuantity(5);

        allTtShirts.add(savedTShirt2);
        tShirtsByColor.add(savedTShirt2);

        TShirt newTShirt3 = new TShirt();
        newTShirt3.setSize("Large");
        newTShirt3.setColor("Blue");
        newTShirt3.setDescription("T-Shirt");
        newTShirt3.setPrice(new BigDecimal("10.99"));
        newTShirt3.setQuantity(5);

        TShirt savedTShirt3 = new TShirt();
        savedTShirt3.setId(30);
        savedTShirt3.setSize("Large");
        savedTShirt3.setColor("Blue");
        savedTShirt3.setDescription("T-Shirt");
        savedTShirt3.setPrice(new BigDecimal("10.99"));
        savedTShirt3.setQuantity(5);

        allTtShirts.add(savedTShirt3);
        tShirtsBySize.add(savedTShirt3);

        doReturn(savedTShirt1).when(client).createTShirt(newTShirt1);
        doReturn(savedTShirt2).when(client).createTShirt(newTShirt2);
        doReturn(savedTShirt3).when(client).createTShirt(newTShirt3);
        doReturn(savedTShirt3).when(client).getTShirt(30);
        doReturn(savedTShirt1).when(client).getTShirt(54);
        doReturn(savedTShirt2).when(client).getTShirt(20);

        doReturn(tShirtsByColor).when(client).getTShirtByColour("Red");
        doReturn(tShirtsBySize).when(client).getTShirtBySize("Medium");
        doReturn(allTtShirts).when(client).getAllTShirts();
    }

    private void setUpInvoiceRepositoryMock() {
        invoiceRepository = mock(InvoiceRepository.class);

        Invoice invoice = new Invoice();
        invoice.setName("John Jake");
        invoice.setStreet("street");
        invoice.setCity("Charlotte");
        invoice.setState("NC");
        invoice.setZipcode("83749");
        invoice.setItemType("T-Shirt");
        invoice.setItemId(54);
        invoice.setUnitPrice(new BigDecimal("19.99"));
        invoice.setQuantity(2);
        invoice.setSubtotal(new BigDecimal("39.98"));
        invoice.setTax(new BigDecimal("2"));
        invoice.setProcessingFee(new BigDecimal("1.98"));
        invoice.setTotal(new BigDecimal("43.96"));

        Invoice invoice1 = new Invoice();
        invoice1.setId(20);
        invoice1.setName("John Jake");
        invoice1.setStreet("street");
        invoice1.setCity("Charlotte");
        invoice1.setState("NC");
        invoice1.setZipcode("83749");
        invoice1.setItemType("T-Shirt");
        invoice1.setItemId(54);
        invoice1.setUnitPrice(new BigDecimal("19.99"));
        invoice1.setQuantity(2);
        invoice1.setSubtotal(new BigDecimal("39.98"));
        invoice1.setTax(new BigDecimal("2.00"));
        invoice1.setProcessingFee(new BigDecimal("1.98"));
        invoice1.setTotal(new BigDecimal("43.96"));

        doReturn(invoice1).when(invoiceRepository).save(invoice);
        doReturn(Optional.of(invoice1)).when(invoiceRepository).findById(20L);

        //Get All...
        Invoice savedInvoice1 = new Invoice();
        savedInvoice1.setName("Sandy Beach");
        savedInvoice1.setStreet("123 Main St");
        savedInvoice1.setCity("any City");
        savedInvoice1.setState("NY");
        savedInvoice1.setZipcode("10016");
        savedInvoice1.setItemType("T-Shirt");
        savedInvoice1.setItemId(12);//pretending item exists with this id...
        savedInvoice1.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice1.setQuantity(2);
        savedInvoice1.setSubtotal(savedInvoice1.getUnitPrice().multiply(new BigDecimal(savedInvoice1.getQuantity())));
        savedInvoice1.setTax(savedInvoice1.getSubtotal().multiply(new BigDecimal("0.06")));
        savedInvoice1.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice1.setTotal(savedInvoice1.getSubtotal().add(savedInvoice1.getTax()).add(savedInvoice1.getProcessingFee()));
        savedInvoice1.setId(22);

        Invoice savedInvoice2 = new Invoice();
        savedInvoice2.setName("Rob Bank");
        savedInvoice2.setStreet("888 Main St");
        savedInvoice2.setCity("any town");
        savedInvoice2.setState("NJ");
        savedInvoice2.setZipcode("08234");
        savedInvoice2.setItemType("Console");
        savedInvoice2.setItemId(120);//pretending item exists with this id...
        savedInvoice2.setUnitPrice(new BigDecimal("129.50"));//pretending item exists with this price...
        savedInvoice2.setQuantity(1);
        savedInvoice2.setSubtotal(savedInvoice2.getUnitPrice().multiply(new BigDecimal(savedInvoice2.getQuantity())));
        savedInvoice2.setTax(savedInvoice2.getSubtotal().multiply(new BigDecimal("0.08")));
        savedInvoice2.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice2.setTotal(savedInvoice2.getSubtotal().add(savedInvoice2.getTax()).add(savedInvoice2.getProcessingFee()));
        savedInvoice2.setId(12);

        Invoice savedInvoice3 = new Invoice();
        savedInvoice3.setName("Sandy Beach");
        savedInvoice3.setStreet("123 Broad St");
        savedInvoice3.setCity("any where");
        savedInvoice3.setState("CA");
        savedInvoice3.setZipcode("90016");
        savedInvoice3.setItemType("Game");
        savedInvoice3.setItemId(19);//pretending item exists with this id...
        savedInvoice3.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice3.setQuantity(4);
        savedInvoice3.setSubtotal(savedInvoice3.getUnitPrice().multiply(new BigDecimal(savedInvoice3.getQuantity())));
        savedInvoice3.setTax(savedInvoice3.getSubtotal().multiply(new BigDecimal("0.09")));
        savedInvoice3.setProcessingFee(BigDecimal.ZERO);
        savedInvoice3.setTotal(savedInvoice3.getSubtotal().add(savedInvoice3.getTax()).add(savedInvoice3.getProcessingFee()));
        savedInvoice3.setId(73);

        List<Invoice> allList = new ArrayList<>();
        allList.add(savedInvoice1);
        allList.add(savedInvoice2);
        allList.add(savedInvoice3);

        doReturn(allList).when(invoiceRepository).findAll();
    }

    private void setUpProcessingFeeRepositoryMock() {

        processingFeeRepository = mock(ProcessingFeeRepository.class);

        ProcessingFee processingFee = new ProcessingFee();
        processingFee.setFee(new BigDecimal("1.98"));
        processingFee.setProductType("T-Shirt");

        doReturn(Optional.of(processingFee)).when(processingFeeRepository).findById("T-Shirt"); //findByProductType

    }

    private void setUpTaxRepositoryMock() {
        taxRepository = mock(TaxRepository.class);

        Tax taxNC = new Tax();
        taxNC.setRate(new BigDecimal(".05"));
        taxNC.setState("NC");

        Tax taxNY = new Tax();
        taxNY.setRate(BigDecimal.ZERO);
        taxNY.setState("NY");

        doReturn(Optional.of(taxNC)).when(taxRepository).findById("NC"); //findByState
        doReturn(Optional.of(taxNY)).when(taxRepository).findById("NY"); //findByState

    }


}