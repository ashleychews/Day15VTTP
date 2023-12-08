package vttp.ssf.day15workshop.day15workshop.controller;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import vttp.ssf.day15workshop.day15workshop.model.Contact;
import vttp.ssf.day15workshop.day15workshop.repo.Contacts;

@Controller
@RequestMapping("/address")
public class AddressbookController {

    @Autowired
    Contacts conRepo;

    //add contacts
    @GetMapping("/contact")
    public String contact (Model model) {
        model.addAttribute("contact", new Contact());
        return "contact"; //return contact.html

    }

    //validating form fields
    @PostMapping("/saveContact")
    public String saveContact(@Valid @ModelAttribute("contact") Contact contactForm, BindingResult result, Model model)
    throws FileNotFoundException {

        //if got error, it will stay on page
        if (result.hasErrors()) {
            return "contact"; //returns contact.html
        }

        // Calculate age based on birthDay
        contactForm.calculateAge();
        // Validate age range
        if (contactForm.getAge() < 10 || contactForm.getAge() > 100) {
            result.addError(new FieldError("contact", "birthDay", "Age must be between 10 and 100 years old"));
            return "contact"; // if got error, stay on page
    }

        // //rmb to call save function
        Boolean returnResult = conRepo.save(contactForm);

        model.addAttribute("savedContact", contactForm);
        return "showcontact"; //returns showcontact.html
    }

    //maps the controller method to handle GET requests to /contact/<id>
    @GetMapping("/contact/{id}") 
    public String getContactId(@PathVariable("id") String id, Model model) throws ParseException {

        // Retrieve contact details by ID
        Contact contact = conRepo.getContactById(id);

        // Check if contact is found
        if (contact != null) {
            model.addAttribute("contact", contact);
            return "showcontact"; //returns showcontact.html
        } else {
            return "error";
        }
    }

    //contactlist to generate list of HTML links for contacts in dir
    @GetMapping("/list")
    public String contactList(Model model) {
        String dataDir = "C://data";
        //Calls the listFiles method to obtain a set of all file names in the specified dataDir directory
        Set<String> dataFiles = conRepo.listFiles(dataDir);
        //Initializes a new set to store the modified file names (without the ".txt" extension)
        Set<String> modifyFile = new HashSet<String>();

        //  Iterates over each file name in the dataFiles set
        for (String file: dataFiles) {
            String modified = file.replace(".txt", "");
            //Adds the modified file name to the modifiedFiles set
            modifyFile.add(modified);
        }

        // Add the modified file names to the model attribute "contacts"
        model.addAttribute("contacts", modifyFile.toArray(new String [dataFiles.size()]));

        return "contacts"; // Return contacts.html
    }
    
}