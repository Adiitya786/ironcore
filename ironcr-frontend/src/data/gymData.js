const gymInfo = {
  name: "IronCore Fitness",
  tagline: "Train smarter. Get stronger. Build your journey.",
  description:
    "IronCore Fitness is a premium modern gym offering expert guidance, state-of-the-art equipment, and a clean professional environment built for results.",
  address: "Sector 12, Near City Mall, Gurugram, Haryana 122001",
  phone: "+91 98765 43210",
  email: "hello@ironcorefitness.com",
  openingHours: [
    { day: "Monday - Friday", time: "5:00 AM - 11:00 PM" },
    { day: "Saturday - Sunday", time: "6:00 AM - 10:00 PM" },
  ],
};

export const stats = [
  { value: "500+", label: "Members" },
  { value: "20+", label: "Equipment" },
  { value: "5+", label: "Expert Trainers" },
  { value: "7", label: "Days Open" },
];

export const whyChooseUs = [
  { title: "Modern Equipment", desc: "State-of-the-art machines maintained daily for peak performance.", iconKey: "Dumbbell" },
  { title: "Expert Trainers", desc: "Certified professionals with years of real-world experience.", iconKey: "Award" },
  { title: "Flexible Timings", desc: "Early mornings to late nights — train on your schedule.", iconKey: "Clock" },
  { title: "Clean Facility", desc: "Hygiene-first approach with frequent deep cleaning.", iconKey: "Sparkles" },
  { title: "Personal Guidance", desc: "Tailored plans based on your body, goals, and lifestyle.", iconKey: "Target" },
  { title: "Multiple Programs", desc: "From strength to weight loss — something for every goal.", iconKey: "Layers" },
];

export const programs = [
  {
    id: "weight-loss",
    title: "Weight Loss",
    desc: "Burn fat with structured cardio and nutrition-aligned strength workouts.",
    image: "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=800&q=80",
    benefits: ["Calorie-focused programming", "Cardio + strength balance", "Progress tracking"],
  },
  {
    id: "muscle-gain",
    title: "Muscle Gain",
    desc: "Progressive overload plans designed to build lean muscle safely.",
    image: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800&q=80",
    benefits: ["Hypertrophy-focused splits", "Protein-focused guidance", "Recovery protocols"],
  },
  {
    id: "strength-training",
    title: "Strength Training",
    desc: "Build raw strength with compound lifts and periodized programming.",
    image: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=800&q=80",
    benefits: ["Compound lifts", "Periodization cycles", "Form coaching"],
  },
  {
    id: "personal-training",
    title: "Personal Training",
    desc: "One-on-one coaching tailored to your body and lifestyle.",
    image: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&q=80",
    benefits: ["Custom plans", "Direct feedback", "Accountability"],
  },
  {
    id: "functional-training",
    title: "Functional Training",
    desc: "Improve mobility, balance, and real-world movement patterns.",
    image: "https://images.unsplash.com/photo-1549068106-b024baf0c729?w=800&q=80",
    benefits: ["Mobility focus", "Core stability", "Athletic movement"],
  },
  {
    id: "general-fitness",
    title: "General Fitness",
    desc: "Balanced routines to improve overall health and energy.",
    image: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800&q=80",
    benefits: ["Balanced routines", "Energy improvement", "Long-term health"],
  },
];

export const equipment = {
  strength: [
    { name: "Squat Rack", desc: "Heavy-duty rack for safe compound lifts.", image: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&q=80" },
    { name: "Smith Machine", desc: "Guided barbell path for controlled strength.", image: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=600&q=80" },
    { name: "Leg Press", desc: "Targeted lower body strength with safety rails.", image: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=600&q=80" },
    { name: "Chest Press", desc: "Upper body pushing power with adjustable resistance.", image: "https://images.unsplash.com/photo-1549068106-b024baf0c729?w=600&q=80" },
    { name: "Lat Pulldown", desc: "Back width and strength development.", image: "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=600&q=80" },
    { name: "Cable Machine", desc: "Versatile attachments for full-body training.", image: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=600&q=80" },
    { name: "Bench Press", desc: "Classic bench for barbell and dumbbell work.", image: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=600&q=80" },
    { name: "Dumbbells", desc: "Range of weights for progressive training.", image: "https://images.unsplash.com/photo-1549068106-b024baf0c729?w=600&q=80" },
    { name: "Barbells", desc: "Olympic standard bars for compound lifts.", image: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=600&q=80" },
  ],
  cardio: [
    { name: "Treadmill", desc: "High-performance running with incline and interval modes.", image: "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=600&q=80" },
    { name: "Cross Trainer", desc: "Low-impact full-body cardio with adjustable resistance.", image: "https://images.unsplash.com/photo-1549068106-b024baf0c729?w=600&q=80" },
    { name: "Exercise Bike", desc: "Upright and recumbent bikes for endurance work.", image: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&q=80" },
    { name: "Rowing Machine", desc: "Full-body pull and push with cardiovascular benefit.", image: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=600&q=80" },
    { name: "Stair Climber", desc: "Calorie-intensive vertical climbing workout.", image: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=600&q=80" },
  ],
  functional: [
    { name: "Kettlebells", desc: "Dynamic strength, endurance, and mobility training.", image: "https://images.unsplash.com/photo-1549068106-b024baf0c729?w=600&q=80" },
    { name: "Battle Ropes", desc: "High-intensity interval training for power and endurance.", image: "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=600&q=80" },
    { name: "Resistance Bands", desc: "Portable progressive resistance for all levels.", image: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=600&q=80" },
    { name: "Medicine Balls", desc: "Explosive and rotational strength exercises.", image: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=600&q=80" },
    { name: "Functional Rig", desc: "Multi-station rig for pull-ups, dips, and suspension.", image: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&q=80" },
  ],
};

export const trainers = [
  {
    id: "rahul-sharma",
    name: "Rahul Sharma",
    specialization: "Strength & Conditioning",
    experience: "5+ Years",
    bio: "National-level competitor turned coach. Rahul specializes in periodized strength programming and injury prevention.",
    image: "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=600&q=80",
  },
  {
    id: "neha-verma",
    name: "Neha Verma",
    specialization: "Weight Loss & Nutrition",
    experience: "8+ Years",
    bio: "Certified nutrition coach with a background in functional fitness. Neha helps clients achieve sustainable body transformation.",
    image: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=600&q=80",
  },
  {
    id: "kiran-mishra",
    name: "Kiran Mishra",
    specialization: "Functional Training",
    experience: "4+ Years",
    bio: "Former athlete focused on mobility, balance, and real-world performance for everyday strength.",
    image: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&q=80",
  },
  {
    id: "arjun-reddy",
    name: "Arjun Reddy",
    specialization: "Personal Training",
    experience: "6+ Years",
    bio: "Dedicated to personalized coaching with a focus on progressive overload, form mastery, and long-term consistency.",
    image: "https://images.unsplash.com/photo-1500648767791-00dccff333e4?w=600&q=80",
  },
];

export const membershipPlans = [
  {
    id: "basic",
    name: "Basic",
    price: "₹999",
    period: "month",
    features: ["Gym Access", "Cardio Area", "Weight Training", "Shower Access"],
    suitableFor: "Beginners exploring fitness without a big commitment.",
  },
  {
    id: "standard",
    name: "Standard",
    price: "₹2499",
    period: "3 Months",
    features: ["Gym Access", "Cardio Area", "Weight Training", "Fitness Assessment", "Locker Storage"],
    suitableFor: "Regular members who want consistent progress and guidance.",
  },
  {
    id: "premium",
    name: "Premium",
    price: "₹7999",
    period: "year",
    features: ["Full Gym Access", "Personal Training Sessions", "Fitness Assessment", "Diet Guidance", "Priority Booking"],
    suitableFor: "Committed athletes seeking full support and accountability.",
  },
];

export const testimonials = [
  { stars: 5, review: "Great equipment, clean environment, and supportive trainers. I have never felt more motivated.", name: "Rahul K." },
  { stars: 5, review: "The personal training program changed my fitness journey completely. Highly recommend.", name: "Priya M." },
  { stars: 5, review: "Flexible timings and modern facilities make it easy to stay consistent even with a busy schedule.", name: "Amit S." },
];

export const faqs = [
  { q: "Do you offer a free trial?", a: "Yes. You can book a free trial session to experience our facilities before committing." },
  { q: "What are your opening hours?", a: "Monday to Friday: 5:00 AM - 11:00 PM. Saturday and Sunday: 6:00 AM - 10:00 PM." },
  { q: "Do you have personal trainers?", a: "Yes. Our certified trainers offer personalized coaching and program design." },
  { q: "Can beginners join?", a: "Absolutely. We have beginner-friendly programs and guided onboarding sessions." },
  { q: "Do you provide diet guidance?", a: "Our Premium plan includes diet guidance, and assessments are available for other members." },
  { q: "What membership plans do you offer?", a: "Basic (monthly), Standard (3 months), and Premium (annual) plans with different features." },
  { q: "Is parking available?", a: "Yes. Dedicated parking is available for members and trial guests." },
];

export const galleryImages = [
  { src: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800&q=80", category: "Gym" },
  { src: "https://images.unsplash.com/photo-1549068106-b024baf0c729?w=800&q=80", category: "Gym" },
  { src: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=800&q=80", category: "Equipment" },
  { src: "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=800&q=80", category: "Equipment" },
  { src: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&q=80", category: "Trainers" },
  { src: "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=800&q=80", category: "Trainers" },
  { src: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800&q=80", category: "Members" },
  { src: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=800&q=80", category: "Members" },
];

export const facilities = [
  { title: "Weight Training Area", desc: "Heavy-duty racks and free weights for strength development." },
  { title: "Cardio Zone", desc: "Treadmills, bikes, and rowers for endurance work." },
  { title: "Functional Training Area", desc: "Rig, ropes, and open space for dynamic movement." },
  { title: "Stretching Area", desc: "Dedicated zone for mobility and recovery work." },
  { title: "Changing Rooms", desc: "Clean, private rooms with shower and soap facilities." },
  { title: "Lockers", desc: "Secure personal storage for your belongings." },
  { title: "Washrooms", desc: "Maintained hygiene facilities for all members." },
  { title: "Drinking Water", desc: "Filtered water stations throughout the gym." },
  { title: "Parking", desc: "Dedicated parking for members and guests." },
];

export { gymInfo };
export default gymInfo;
