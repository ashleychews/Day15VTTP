package vttp.ssf.day15workshop.day15workshop.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import vttp.ssf.day15workshop.day15workshop.model.Contact;


@Repository
public class Contacts {
    
    final String dataDir = "C://data";
    
    private List<Contact> contacts;

    //constructor
    public Contacts() throws ParseException {
        if (contacts == null) {
            //initiate an empty list
            contacts = new ArrayList<Contact>();
        }
    }

    //generate 8 character long hex string
    private String generateId(int numChars) {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(rand.nextInt()));
        }
        
        return sb.toString().substring(0, numChars);
    }


    //Save contact
    public Boolean save(Contact contact) throws FileNotFoundException {
        //generate a 8 digits hex id
        String id = generateId(8);
        //save contact with the generate id
        contact.setId(id);

        Boolean result = false;

        result = contacts.add(contact);

        //save file as dir/id.txt
        File f = new File(dataDir + "/" + id + ".txt");
        OutputStream os = new FileOutputStream(f, true);
        PrintWriter pw = new PrintWriter(os);
        // write each attribute on a new line
        pw.println(contact.getName());
        pw.println(contact.getEmail());
        pw.println(contact.getPhoneNo());
        pw.println(contact.getBirthDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        pw.println(contact.getAge());

        pw.flush();
        pw.close();

        return result;

    }

    //get contact by ID
    public Contact getContactById(String id) throws ParseException {

        Contact contact = new Contact();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        File file = new File(dataDir + "/" + id + ".txt");
        Path filePath = file.toPath();
        Charset charset = Charset.forName("UTF-8");

        try {
            List<String> lines = Files.readAllLines(filePath, charset);

            //System.out.println("Reading contact info from file: " + filePath); // DEBUG
            //System.out.println("Number of lines read: " + lines.size()); // DEBUG
            //System.out.println("File contents: " + lines); // DEBUG

            if (lines.size() >= 5) {
                contact.setId(id);
                contact.setName(lines.get(0));
                contact.setEmail(lines.get(1));
                contact.setPhoneNo(lines.get(2));
                LocalDate birthDay = LocalDate.parse(lines.get(3), formatter);
                contact.setBirthDay(birthDay);
                contact.setAge(Integer.parseInt(lines.get(4)));

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact info not found");
            }

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact info not found");
        }

        return contact;
    }

    // List files in the directory, filter out directories, and map to file names
    public Set<String> listFiles(String dir) {
    return Stream.of(new File(dir).listFiles())
            .filter(file -> !file.isDirectory())
            .map(File::getName)
            .collect(Collectors.toSet()); //collect file names into a set
    }
}