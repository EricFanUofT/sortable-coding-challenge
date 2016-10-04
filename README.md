# sortable-coding-challenge
## Matching product specifications from one data source to product pricings from another data source

The program product_matching.java contains four methods:
* public static LinkedList<Product> parse_products(FileReader fr)
* public static LinkedList<Listing> parse_listings(FileReader fr)
* public static void matching(FileWriter fw, LinkedList<Product> products_list, LinkedList<Listing> listings_list)
* public static void main(String[] args)

**_parse_products(FileReader fr)_** reads and parses the JSON objects from an input data source (i.e., "products.txt") line by line, and saves the result in a list of newly defined Product objects

**_parse_listings(FileReader fr)_** reads and parses the JSON objects from another input data source (i.e., "listings.txt"), and saves the result in a list of newly defined Listing objects

**_public static void matching(FileWriter fw, LinkedList\<Product\> products_list, LinkedList\<Listing\> listings_list)_** iterates through the entire list of Product objects and find all the matching Listing objects.  For a Listing object to match a Product object, the following criteria are used:
* the manufacturer must be the same
* the Listing title must contain the Product family (if one is given) and the Product model
* if the word "accessory" or "accessories" is in the Listing title, make sure it comes after the model

  For example, 

  the Listing with title *"Nikon 85mm f/3.5 G VR AF-S DX ED Micro-Nikkor Lens + UV Filter + Accessory Kit for Nikon D300s, D300, D40,     D60, D5000, D7000, D90, D3000 & D3100 Digital SLR Cameras"* should not be matched to the Product *"Nikon_D3000"*,

  since *"D3000"* comes after the word *"Accessory"*

The Result objects are saved to the file "results.txt" in the output directory and have been checked using the validator service.

To run my solution, please clone my repository and run *bash go.sh* in the terminal

