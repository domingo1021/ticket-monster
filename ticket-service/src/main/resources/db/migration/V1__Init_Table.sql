-- V1__Create_Ticket_Table.sql
-- Create the Ticket table
CREATE TABLE IF NOT EXISTS ticket (
  id SERIAL PRIMARY KEY,
  concert VARCHAR(255)  NOT NULL,
  date DATE NOT NULL,
  venue VARCHAR(255) NOT NULL,
  price NUMERIC(10, 2) NOT NULL,
  status VARCHAR(50) NOT NULL
);

-- Insert some initial data
INSERT INTO ticket (concert, date, venue, price, status)
VALUES
    ('Concert A', '2024-09-01', 'Venue A', 50.00, 'AVAILABLE'),
    ('Concert B', '2024-09-05', 'Venue B', 60.00, 'AVAILABLE'),
    ('Concert C', '2024-09-10', 'Venue C', 70.00, 'AVAILABLE');
