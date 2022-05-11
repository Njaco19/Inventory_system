package com.inventory.InventorySystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.CannotProceedException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@Controller
public class InventorySystemApplication {
	private static Inventory inventory;

	public static void main(String[] args) throws InvalidNameException {
		inventory = Inventory.getInstance();
		inventory.addItem(new Product("Pan", 13), 20);
		inventory.addItem(new Product("Pot", 42), 20);
		WarehouseWorker w = new WarehouseWorker("Amazon Drone #42");
		Salesman s = new Salesman("Sale S. Mann");
		Manager m = new Manager("Richard Tator");
		Store imerco = new Store("Imerco");
		Store petro = new Store("Petrograd City Pots and Pans for vodka creation");
		s.createOrder(imerco);
		int id = s.createOrder(petro);
		s.addCustomer(imerco);
		s.addCustomer(petro);
		Optional<Order> found = Optional.empty();
		for (Order x : inventory.getOrders()) {
			if (x.getOrderID() == id) {
				found = Optional.of(x);
				break;
			}
		}
		Order o = found.get();
		o.addProduct(new Product("Pot", 23), 4);
		o.addProduct(new Product("Toilet Bowl", 1), 42);
		o.addProduct(new Product("Pan", 69), 10);
		try{
			w.beginOrder(o);
		}catch(CannotProceedException e) {
			e.printStackTrace();
		}
		SpringApplication.run(InventorySystemApplication.class, args);

	}

	@GetMapping("/")
	public String getHomePage(){
		return "homepage";
	}

	@GetMapping("/allOrders/createOrder")
	public String createOrder(@RequestParam(value = "salesmanName", required = false) String salesmanName,
							  Model model){
		CommObject cm = new CommObject();
		if (salesmanName != null){
			cm.setName(salesmanName);
		}
		model.addAttribute("commobject", cm);
		return "createOrder";
	}

	@PostMapping("/allOrders/createOrder")
	public String submitCreatedOrder(@ModelAttribute CommObject co, Model model){
		String username = co.getName();
		if (Inventory.getInstance().getEmployees().stream().noneMatch(s -> s.getUsername().equals(username))){
			return "homepage";
		}

		Salesman salesman = (Salesman) Inventory.getInstance().getEmployee(username);

		if (salesman.getCustomers().stream().noneMatch(s -> s.getName().equals(co.getStore()))){
			return "homepage";
		}

		Store store = salesman.getCustomers().stream()
				.filter(s -> s.getName().equals(co.getStore()))
				.findFirst().get();
		salesman.createOrder(store);
		return "homepage";
	}

	@GetMapping("/allOrders/addToOrder")
	public String addToOrder(@RequestParam(value = "id", required = false) String id, Model model){
		CommObject cm = new CommObject();
		if (id != null) {
			cm.setId(Integer.parseInt(id));
		}
		model.addAttribute("allProducts", Inventory.getInstance().getStock());
		model.addAttribute("commobject", cm);
		return "addToOrder";
	}

	@PostMapping("/allOrders/addToOrder")
	public String submitAddToOrder(@ModelAttribute CommObject co, Model model){
		String productName = co.getName();
		if(inventory.isInStock(productName)){
			if(inventory.getOrders().stream().anyMatch(o -> o.getOrderID() == co.getId())){
				Order o = inventory.getOrders().stream().filter(ord -> ord.getOrderID() == co.getId()).findFirst().get();
				o.addProduct(inventory.getStock()
						.keySet()
						.stream()
						.filter(p -> p.getName().equals(productName))
						.findFirst()
						.get(),co.getAmount());

			}else{
				return "homepage";
			}
		}else{
			return "homepage";
		}
		return "allOrders";
	}

	//Responds to /allOrders/status
	@RequestMapping(value = "/allOrders", method = RequestMethod.GET)
	public String getAllOrders(@RequestParam(value = "status", required = false, defaultValue = "all") String status,
							   @RequestParam(value = "salesman", required = false) String salesman,
							   Model model){

		if (salesman != null) {
			model.addAttribute("allOrders", Inventory.getInstance().getOrders().stream()
					.filter(s -> s.getSalesman().getUsername().equals(salesman))
					.collect(Collectors.toList()));
		} else {
			switch (status.toLowerCase()) {
				case ("pending"):
					model.addAttribute("allOrders", Inventory.getInstance().getOrders().stream()
							.filter(s -> s.getOrderStatus() == OrderStatus.PENDING)
							.collect(Collectors.toList()));
					break;
				case ("processing"):
					model.addAttribute("allOrders", Inventory.getInstance().getOrders().stream()
							.filter(s -> s.getOrderStatus() == OrderStatus.PROCESSING)
							.collect(Collectors.toList()));
					break;
				case ("completed"):
					model.addAttribute("allOrders", Inventory.getInstance().getOrders().stream()
							.filter(s -> s.getOrderStatus() == OrderStatus.COMPLETED)
							.collect(Collectors.toList()));
					break;
				default:
					model.addAttribute("allOrders", Inventory.getInstance().getOrders());
					break;
			}
		}
		return "allOrders";
	}

	@RequestMapping(value = "/seeWorker/{worker}", method = RequestMethod.GET)
	public String seeWorker(@PathVariable String worker, Model model){
		WarehouseWorker thisWorker = (WarehouseWorker) Inventory.getInstance().getEmployee(worker);
		model.addAttribute("worker", thisWorker.getUsername());
		if (thisWorker.getWorkingOn() != null) {
			model.addAttribute("orderID", thisWorker.getWorkingOn().getOrderID());
			model.addAttribute("order", thisWorker.getWorkingOn());
		} else{
			model.addAttribute("orderID", "No current ID");
			model.addAttribute("order", "No current order being worked on");
		}
		return "seeWorker";
	}

	@RequestMapping(value = "/seeSalesman/{salesman}", method = RequestMethod.GET)
	public String seeSalesman(@PathVariable String salesman, Model model){
		Salesman thisSalesman = (Salesman) Inventory.getInstance().getEmployee(salesman);
		model.addAttribute("salesman", thisSalesman.getUsername());
		model.addAttribute("allStores", thisSalesman.getCustomers());
		return "seeSalesman";
	}

	@GetMapping("/createCustomer")
	public String createCustomer (@RequestParam(value = "salesmanName", defaultValue = "Input name here", required = false)
											  String salesmanName, Model model){
		CommObject cm = new CommObject();
		cm.setString3(salesmanName);
		model.addAttribute("commobject", cm);
		return "createCustomer";
	}

	@PostMapping("/createCustomer")
	public String submitCreatedCustomer(@ModelAttribute CommObject co, Model model){
		Salesman sm = (Salesman) Inventory.getInstance().getEmployee(co.getString3());
		Store s = new Store(co.getName(), co.getStore(), co.getAmount());
		sm.addCustomer(s);
		return "homepage";
	}

	//Responds to /allWorkers?id="username"
	@GetMapping("/allWorkers")
	public String getAllOrders(Model model) {
		List<WarehouseWorker> ww = Inventory.getInstance().getEmployees().stream()
				.filter(s -> s instanceof WarehouseWorker)
				.map(s -> (WarehouseWorker) s)
				.collect(Collectors.toList());
		model.addAttribute("allWW", ww);
		List<Salesman> sm = Inventory.getInstance().getEmployees().stream()
				.filter(s -> s instanceof Salesman)
				.map(s -> (Salesman) s)
				.collect(Collectors.toList());
		model.addAttribute("allSM", sm);
		List<Manager> mn = Inventory.getInstance().getEmployees().stream()
				.filter(s-> s instanceof Manager)
				.map(s->(Manager) s)
				.collect(Collectors.toList());
		model.addAttribute("allMN",mn);
		return "allWorkers";
	}


	@GetMapping("/allWorkers/createUser")
	public String getCreateUserPage(Model model){
		CommObject cm = new CommObject();
		model.addAttribute("commobject", cm);
		return "createUser";
	}

	@PostMapping("/allWorkers/createUser")
	public String submitCreatedUser(@ModelAttribute CommObject co, Model model) throws InvalidNameException {
		switch (co.getString3().toLowerCase()){
			case "warehouse worker":
				WarehouseWorker ww = new WarehouseWorker(co.getName());
				break;
			case "salesman":
				Salesman sm = new Salesman(co.getName());
				break;
			case "manager":
				Manager mn = new Manager(co.getName());
				break;
			default:
				throw new InvalidParameterException("Cannot create worker with job " + co.getString3());
		}
		return "homepage";
	}

	@PostMapping("/advanceOrder")
	public String advanceOrder(@RequestHeader("Referer") String header,
							   @RequestParam( value = "orderID", required = true) String orderID,
							   Model model){
		try{
			Inventory.getInstance().getOrders().stream()
					.filter(s -> s.getOrderID() == Integer.parseInt(orderID))
					.findFirst().get().updateOrderStatus();
		} catch (UnsupportedOperationException | CannotProceedException e) {
			return e.getMessage();
		}
		return "redirect:"+ header; 	//I know this is not optimal, but damn Jackie, it works on my machine
	}

	@GetMapping("/allOrders/viewOrder")
	public String viewOrder(@RequestParam (value = "id", required = true)String orderID, Model model){
		model.addAttribute(Inventory.getInstance().getOrders().stream().filter(o -> o.getOrderID() == Integer.parseInt(orderID)).findFirst().get());
		return "viewOrder";
	}
}
